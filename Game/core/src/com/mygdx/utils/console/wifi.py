import usocket as socket
import network
from machine import Pin
#192.168.0.15

# Matrix of buttons
rows = [Pin(1, Pin.OUT), Pin(2, Pin.OUT), Pin(3, Pin.OUT)]
columns = [Pin(4, Pin.IN, Pin.PULL_UP), Pin(5, Pin.IN, Pin.PULL_UP), Pin(6, Pin.IN, Pin.PULL_UP)]
led = Pin(7, Pin.OUT)

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

# Configurar el servidor socket
server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.bind(('0.0.0.0', 8080))  # Escucha en todas las interfaces en el puerto 8080
server.listen(1)

print('Esperando conexiones...')
conn, addr = server.accept()
print('Conexión establecida desde', addr)

# Loop principal
try:
    while True:
        led.on()  # Enciende el LED
        for i, row in enumerate(rows):
            row.value(0)
            for j, column in enumerate(columns):
                if not column.value():
                    print(f"Row: {i+1}  Column: {j+1}")

                    # Enviar datos al cliente (computadora)
                    conn.sendall(f"Row: {i+1}  Column: {j+1}\n".encode('utf-8'))

            row.value(1)

except KeyboardInterrupt:
    pass

finally:
    led.off()
    print("Finished.")
    conn.close()
    server.close()
