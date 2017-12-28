# metawipe
[![Build Status](https://travis-ci.org/kivimango/metawipe.svg?branch=master)](https://travis-ci.org/kivimango/metawipe) [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

Small command-line tool to remove all metadata / exif records of your photos to give you back the control over your security and privacy. 

It is useful before uploading photos to cloud like Facebook, Google, etc if you don't want to share your GPS location and other sensitive data with anyone.

## Supported formats
  - jpg
  - jpeg
  - tiff

# Requirements
You need at least Java JRE 1.8 to run this software.

## Usage
* Navgiate to the folder containing the metawipe.jar file
* Open a terminal in this folder
* type _java -jar metawipe.jar <args>_

_<args>_ can be :
 - ***-f*** : Clears metadata of one particular file.
 Note: Nothing is written on the console after succesful operation (and the   program returns 0).
Error messages still displayed.
 Example: ```java -jar metawipe.jar -f /path/to/photo.jpg```
 - ***-d***: Clears every supported file formats in the given directory and its subdirectories.
 Example: ```java -jar metawipe.jar -d /path/to/directory/```
- ***-help*** Displays the help.
Example: ```java -jar metawipe.jar -help```

### Installation
* [Download the latest version](https://github.com/kivimango/metawipe/releases) and unzip it somewhere to your directory
* If you want to build from source, clone this repository:
```git clone https://github.com/kivimango/metawipe.git```
Execute the following command in the terminal:
```mvn clean compile assembly:single```
The builded jar will be in the _/target_ frolder

### Contribution
Contributions are welcome ! If you see errors, bugs, please open a [new Issue](https://github.com/kivimango/metawipe/issues), or send a pull request.
### License
MIT

