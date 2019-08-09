# ConsoleServer
- Provides ability to access terminal of remote system.

## Usage
- Run ConsoleServer 
- ConsoleServer will post its IP address on dweet
- Go to https://dweet.io/get/dweets/for/Q3VMJTMJ3M99RF9CVPJ3Q7VF3
- we are using dweet.io to make remote system discoverable, if required dweet could be replaced to different system available on network to listen for remote systems discovery beacons in-order to know remote system's IP. 
- connect to ConsoleServer through TCP IP, you may use netcat utility 
- send commands to remote system terminal and observe output(as root.remoteConsole.Result object) returned.