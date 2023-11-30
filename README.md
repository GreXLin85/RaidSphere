# RaidSphere

RaidSphere is a Virtual Disk solution that allows you to create a RAID 4 array using multiple
SSDs, HDDs or USB Disks attached to Raspberry Pi 4. This project was created at 2023 as a
hobby project and is not intended to be used in production environments. It also has NAS capabilities.

## Supported platforms
| Supported platforms                                           |     |      |
|---------------------------------------------------------------|-----|------|
| Linux                                                         | x64 | x86  |
| MacOS (via [osxfuse](https://osxfuse.github.io/))             | x64 | x86  |
| Windows (via [winfsp](https://github.com/billziss-gh/winfsp/))| x64 | n/a  |

## Installation

### Prerequisites

- Raspberry Pi 4
- USB Hub or Docking Station that has 4 or more USB 3.0 or SATA ports and 1 USB 3.0 port for the Raspberry Pi 4
  connection
- 4 or more Disks (SSD, HDD, USB Disk) that has the same specs (size, speed, etc.)
- 1 SD Card (8GB or more) for the Raspberry Pi 4 OS
- Java 17 (or higher)
    - Not tested in Java 16 or lower

### Steps

1. Install Raspberry Pi OS on the SD Card
2. Install dependencies on the Raspberry Pi 4
    - If you use Linux `sudo apt-get install libfuse-dev`
    - If you use MacOS `brew install --cask osxfuse`
    - If you use Windows `choco install winfsp`
3. Install Java 17 (or higher) on the Raspberry Pi 4
4. Clone this repository
5. Build the project using `./gradlew build`
6. Copy the `build/libs/raid-sphere-1.0-SNAPSHOT.jar` file to the Raspberry Pi 4
7. Create a `raid-sphere` folder in the Raspberry Pi 4 home directory
8. Copy the `raid-sphere-1.0-SNAPSHOT.jar` file to the `raid-sphere` folder
9. Create a `raid-sphere.conf` file in the `raid-sphere` folder
10. Add the following content to the `raid-sphere.conf` file:
    ```properties
    # The disks that will be used in the RAID 4 array
    raid.disks=/dev/sda,/dev/sdb,/dev/sdc
    raid.parity.disk=/dev/sdd
    
    # The path where the RAID 4 array will be mounted
    virtual_disk.mount.path=/mnt/raid

    # Block size in bytes
    virtual_disk.block.size=4096
    ```
11. Run the `raid-sphere-1.0-SNAPSHOT.jar` file using `java -jar raid-sphere-1.0-SNAPSHOT.jar`
12. The RAID 4 array will be created and mounted in the path specified in the `raid-sphere.conf` file
13. You can now use the RAID 4 array as a normal disk
14. You can also access the RAID 4 array using the NAS capabilities
    - The NAS capabilities are not implemented yet