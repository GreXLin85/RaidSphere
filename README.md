# RaidSphere

## Not ready for any use yet

RaidSphere is a Virtual Disk solution that allows you to create a RAID 1 array using multiple
SSDs, HDDs or USB Disks attached to Raspberry Pi 4. This project was created at 2023 as a
hobby project and is not intended to be used in production environments. It also has NAS capabilities.

#### NOTE : RaidSphere is created for being used in Raspberry Pi 4 with Raspberry Pi OS. It is not tested in other platforms or operating systems. It may work in other platforms or operating systems but it is not guaranteed.

## Supported platforms

| Supported platforms                                            |     |     |
|----------------------------------------------------------------|-----|-----|
| Linux                                                          | x64 | x86 |
| MacOS (via [osxfuse](https://osxfuse.github.io/))              | x64 | x86 |
| Windows (via [winfsp](https://github.com/billziss-gh/winfsp/)) | x64 | n/a |

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
4. Clone this repository with `git clone https://github.com/GreXLin85/RaidSphere.git` on your computer
5. Build the project using `./gradlew build` on your computer
6. Copy the `build/libs/raid-sphere-1.0-SNAPSHOT.jar` file to the Raspberry Pi 4
7. Create a `raid-sphere` folder in the Raspberry Pi 4 home directory
8. Copy the `raid-sphere-1.0-SNAPSHOT.jar` file to the `raid-sphere` folder in the Raspberry Pi 4
9. Create a `raid-sphere.conf` file in the `raid-sphere` folder in the Raspberry Pi 4
10. Change the needed variables in the `raid-sphere.conf` file
11. Run the `raid-sphere-1.0-SNAPSHOT.jar` file using `java -jar raid-sphere-1.0-SNAPSHOT.jar` on the Raspberry Pi 4
12. The RAID 4 array will be created and mounted in the path specified in the `raid-sphere.conf` file (default
    is `/mnt/raid-sphere`)
13. You can now use the RAID 4 array as a normal disk
14. You can also access the RAID 4 array using the NAS capabilities
    - The NAS capabilities are not implemented yet

## Configuration

The configuration file is a simple text file that contains the following variables:

| Variable name             | Description                                                                                          | Default value                |
|---------------------------|------------------------------------------------------------------------------------------------------|------------------------------|
| `virtual_disk.mount.path` | The path where the RAID 4 array will be mounted                                                      | `/mnt/raid-sphere`           |
| `virtual_disk.block.size` | The block size of the disks that will be used in the RAID 4 array. The block size must be in bytes   | `4096`                       |
| `raid.disks`              | The paths of the disks that will be used in the RAID 4 array. The paths must be separated by a comma | `/dev/sda,/dev/sdb,/dev/sdc` |
| `raid.parity.disk`        | The path of the disk that will be used as the parity disk                                            | `/dev/sde`                   |
