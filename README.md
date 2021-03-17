# Cyclic Redundancy Check (CRC) and Parity / Dual Parity Check 

University Project on Digital Communications.



# Table of Contents

1. [Cyclic Redundancy Check (CRC)](#my-first-title)
2. [Parity and Dual Parity Check](#my-second-title)
## Cyclic Redundancy Check(CRC)
The goal of this exercise is to implement the error-detecting algorithm CRC. The following operations should be included:
* Generation of randomly selected k - bits long binary numbers (data blocks of k bits with the probability of each bit being 0 or 1 to be equal).
* Calculation of the corresponding CRC to each message sent. A binary number P will be used as a prototype for the calculation of CRC.
* Transmitting of the message and the CRC through a channel with noise, with Bit Error Rate (BER) and receiving of the corrupted message.
* Check the CRC.

For k = 10, P = 110101 και BER = 0.001 calculate for the largest number of messages possible the following:
* Percentage of messages with errors.
* Percentage of messages with errors detected by CRC.
* Percentage of messages with errors that are not detected by CRC.

## Parity and Dual Parity Check
