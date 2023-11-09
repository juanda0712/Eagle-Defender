from machine import Pin
import utime
import bluetooth
import ubluetooth

# Matrix of buttons
rows = [Pin(1, Pin.OUT), Pin(2, Pin.OUT), Pin(3, Pin.OUT)]
columns = [Pin(4, Pin.IN, Pin.PULL_UP), Pin(5, Pin.IN, Pin.PULL_UP), Pin(6, Pin.IN, Pin.PULL_UP)]
led = Pin(7, Pin.OUT)

""" bt = ubluetooth.BLE()
address = bt.config('mac')
bt_address_str = ':'.join(['%02X' % b for b in address[1]])
print(bt_address_str) """

#BT Address: 28:CD:C1:0B:8D:8C

# Bluetooth Configuration
_UART_SERVICE_UUID = bluetooth.UUID("6E400001-B5A3-F393-E0A9-E50E24DCCA9E")
_UART_RX_CHAR_UUID = bluetooth.UUID("6E400002-B5A3-F393-E0A9-E50E24DCCA9E")

class BLESimpleCentral:
    def __init__(self, ble):
        self._ble = ble
        self._ble.active(True)
        self._reset()

    def _reset(self):
        self._conn_handle = None
        self._tx_handle = None

    def _irq(self, event, data):
        # Define your event handling logic here.
        pass

    def is_connected(self):
        return self._conn_handle is not None and self._tx_handle is not None

    def connect(self, addr_type=None, addr=None, callback=None):
        # Implement the connection logic here.
        pass

    def write(self, v):
        # Implement the data transmission logic here.
        pass

    def demo(self):
        # Implement your demo logic here.
        pass

ble = bluetooth.BLE()
central = BLESimpleCentral(ble)

# Scanning and Bluetooth loop
while True:
    try:
        led.on()  # Enciende el LED
        for i, row in enumerate(rows):
            row.value(0)
            for j, column in enumerate(columns):
                if not column.value():
                    print(f"Row: {i+1}  Column: {j+1}")

                    # Check button state and send data over BLE if pressed
                    if central.is_connected():
                        data_to_send = f"Row: {i+1}  Column: {j+1}"
                        central.write(data_to_send)

            row.value(1)

    except KeyboardInterrupt:
        break

led.off()
print("Finished.")
