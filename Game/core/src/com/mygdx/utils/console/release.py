import machine
import utime
from machine import Pin
import usocket as socket
import network

# Configuración de la conexión WiFi
ssid = 'Rodriguez Montero'
password = 'jdrm0099'

# Conectar a la red WiFi
wifi = network.WLAN(network.STA_IF)
wifi.active(True)
wifi.connect(ssid, password)

# Obtener la dirección IP asignada
while not wifi.isconnected():
    pass
print('Conexión exitosa')
print('IP Address:', wifi.ifconfig()[0])

# Configuración de pines para el joystick
pin_urx = machine.Pin(26)  # GP26 para el eje X
pin_ury = machine.Pin(27)  # GP27 para el eje Y

# Configuración de pines para los botones
rows = [Pin(0, Pin.OUT), Pin(1, Pin.OUT)]
columns = [Pin(2, Pin.IN, Pin.PULL_UP), Pin(3, Pin.IN, Pin.PULL_UP)]
led = Pin(7, Pin.OUT)

# Configurar el servidor socket
server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.bind(('0.0.0.0', 8080))  # Escucha en todas las interfaces en el puerto 8080
server.listen(1)

print('Esperando conexiones...')
conn, addr = server.accept()
print('Conexión establecida desde', addr)

# Función para leer el valor analógico del pin
def read_analog(pin):
    adc = machine.ADC(pin)
    valor = adc.read_u16()
    return valor

# Umbral para considerar que el joystick está en el centro
umbral_centro = 0.6

# Loop principal
try:
    while True:
        # Leer valores analógicos de los pines URX y URY
        valor_urx = read_analog(pin_urx)
        valor_ury = read_analog(pin_ury)

        # Mapear los valores a un rango más manejable (ajusta según tus necesidades)
        valor_urx = (valor_urx - 32768) / 32768.0
        valor_ury = (valor_ury - 32768) / 32768.0

        # Determinar la dirección principal del joystick
        if abs(valor_urx) < umbral_centro and abs(valor_ury) < umbral_centro:
            mensaje_joystick = "0"
        elif abs(valor_urx) > abs(valor_ury):
            if valor_urx > 0:
                mensaje_joystick = "top"
            else:
                mensaje_joystick = "down"
        else:
            if valor_ury > 0:
                mensaje_joystick = "right"
            else:
                mensaje_joystick = "left"
        mensaje_joystick += f"\n eje:"

        # Escaneo de botones
        for i, row in enumerate(rows):
            row.value(0)
            for j, column in enumerate(columns):
                if not column.value():
                    mensaje_botones = f"\nboton:{i+1}{j+1}"
                    print(mensaje_botones)

                    # Enviar datos al cliente (computadora)
                    conn.sendall(mensaje_botones.encode('utf-8'))

            row.value(1)

        # Enviar también información del joystick
        conn.sendall(mensaje_joystick.encode('utf-8'))

        # Esperar un breve período antes de la siguiente lectura
        utime.sleep_ms(100)

except KeyboardInterrupt:
    pass

finally:
    conn.close()
    server.close()