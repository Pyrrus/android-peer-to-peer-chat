# Peer to peer chat using Android, Bluetooth, XML, and Java

## This verson use Bluetooth to talk to the other Android  12/21/16

#### By **Adam Gorbahn**

## Description
This chat app use Bluetooth and encryption to talk to android to other android.

## Specifications

#### 'Menu' Bar

user input      | output
--------------- | -------------
Click on 'Bluetooth' icon | will make list of Bluetooth to make a connect to android.  

user input       | output
---------------- | -------------
Click on 'account' icon | will go to the login Activity. remove the user auth from the app.

#### 'log' Bar

user input      | output
--------------- | -------------
Click on 'chat' icon | will go to chat room.  

#### 'home' Activity

user input                       | output
-------------------------------- | -------------
input text | will make send data to other android with your input

#### 'login' Activity

user input                       | output
-------------------------------- | -------------
input both user email, and password | this will go to the home Activity.

user input                       | output
-------------------------------- | -------------
touch 'Don't have an account? Sign up here!' | this will go to the Create Account Activity.

Have landscape mode when the device on the horizontal.

#### 'Create Account' Activity

user input                       | output
-------------------------------- | -------------
input both user email, name, and passwords | this will go to the home Activity.

user input                       | output
-------------------------------- | -------------
touch 'Already have an account? Log in here!' | this will go to the Login Activity.

Have landscape mode when the device on the horizontal.

## Bugs
* if using Android 2.1. It will not work. (Look in Setup)
* Will sometime connect back when send text and missing the send text.
* Newest Android verson like android 6. it will not display discovered devices list.

## Setup/Installation Requirements

if using Android 2.1
* Disable instant run in setting -> Build, Execution, Deployment -> remove all checkbox
* then Build -> clean project

## Technologies Used

* Java
* XML

### License

*GPL*

Copyright (c) 2016 **Adam Gorbahn**
