# aprj2

Versatile, plugin-extensible APRS server written in Java. 

## Features

### Core

The core features of APRJ2 are extremely limited. They are focused on providing an interface for plugins to interact with APRS-capable devices. 

The core features include:

- Opening serial and networked devices utilizing KISS or TNC2 protocol
- Sending and receiving APRS packets
- Loading plugins

Everything else is left to be implemented as a plugin, possibly with multiple alternatives for the same feature.

### APRS-IS plugin

Exposes a device called `aprsis` that acts as an interface to APRS-IS.

### IGate plugin

Forwards eligible RF packets to APRS-IS and vice versa.

### Digipeater plugin

Repeats eligible packets on a single or multiple devices. Supports multiple path aliases, both traced and untraced. 

### Beacon plugin

Periodically sends beacons with a customizable body.

### DX plugin

Upon detecting an RF packet coming from a far distance, sends a status report with the DX station details: callsign, distance, date and time of reception.

### More?

- Queries
- Weather reports
- Telemetry reporting
- Advanced (IS-augmented and routed) messaging
- Current position reporting based on gpsd
- Remote control via (?) APRS messages
- Traffic analysis
- Web interface
- Web APIs
- ... 

## Installation

### Prerequisites

- Java Development Kit version 17 or newer
- Maven and a network connection to download dependencies

### Building

The simplest way is to utilize the provided `Makefile` script. It is advised to examine it, and potentially add modifications to suit your needs.

- `make build` is equatable to `mvn clean package`.
- `make run` runs the built application.
- `make install` copies the JARs and example configuration files to the system directories.

The more advanced way is to run the Maven commands manually, or to import the project into an IDE and run it from there.

## Customization

### Configuration

All configuration (including plugins) resides in a single XML file. Refer to [CONFIG.md](CONFIG.md) for details.

### Plugins

Plugins are loaded from the classpath. By default, all bundled plugins are installed. 
Add or remove the desired JARs in the target directory. 
Changes will take effect after restarting the application.

#### Writing a plugin

Implement the `Plugin` interface according to JavaDocs present in the code. 

Register/mark the Plugin implementation as a service provider. Like all bundled plugins, you can use Google's AutoService library to simplify this process.

Build the plugin and it's dependencies into one or more JAR files. Fat JARs will work, but try to re-use already present dependencies and save some disk space ;)

## Known errors

- IGate plugin seems not to report properly. Valid packets are definitely sent to the APRS-IS network, 
but they are not showing up on online APRS maps/utilities.  