# Cyclic Redundancy Check (CRC) 

University Project on Digital Communications.

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

### Implementation
The representation of the messages and the necessary operations are implemented with the use of bitwise operators.
### Program Structure
The source code is separated in 4 parts:
* generation of random messages
* calculation of CRC
* calculation of BER probability and corruption of message
* check CRC for errors

### Message Generation
The generated messages are placed in a temporary variable "message", without being permanently stored in memory, because storing such a large amount of data is time consuming and almost impossible.

### CRC Calculation
To calculate the CRC of a message, each bit of the message is checked in order to determine if the Most Significant Bit of the CRC is set. If this is the case, that bit is erased, and we move on to the next bit of the message, performing XOR operation, as described by the CRC algorithm. In case the Most Significant Bit is not set, we proceed to the next bit of the incoming message and repeat the process.

The procedure described above, can be seen in the code segment below, in a function called Compute_CRC( ).

```java
public static int Compute_CRC(int message){ 
    int generator = 0b110101; 
    int msbbit = 0b10000; 
    int mask = 0b111111; 
    
    int messageInput = message; 
    
    for (int i = 9; i >= 0; i--){ 
        if ((crc & msbbit) != 0){ 
            crc = (byte)(crc << 1); 
            crc = crc & mask; 
            crc = ((int)(messageInput & (1 << i)) != 0) ? 
                  (int)(crc | 0x01) : (int)(crc & 0b111110); 
            
            crc = (int)(crc ^ generator); }
            
        else{ 
            crc = (int)(crc << 1); 
            crc = crc & mask; 
            crc = ((int)(messageInput & (1 << i)) != 0) ? 
                  (int)(crc | 0x01) : (int)(crc & 0b111110); 
        } 
        
    }
    return crc; 
}
```

### BER - Message Corruption 
The selection of the messages to be corrupted happens as follows: for each bit of each message, after the CRC has been added, the probability of this bit being corrupted is calculated randomly, applying BER = 0.001. So if a specific bit needs to be corrupted, a random bit of the message the selected bit belongs to, is altered.

The implementation of this procedure can be seen below:

```java
for (int j = 0; j < 15; j++) { 
    // simulate BER 
    if (r.nextInt(1000) < 1) { // 1/1000 BER 
    int bit = r.nextInt(15); 
    
    // XOR bit 
    transmittedMessage = transmittedMessage ^ (1 << bit); 
    bitflip++; totalBitFlips++; } 
}
```

### CRC Testing
To check if a message is valid, a similar method to the calculation of CRC is applied. More specifically, the CRC is calculated before the corruption of the messages and after, and the two values are compared to each other. If the CRC values ​​match, there has been no alteration in the message, otherwise the message has been altered.

The procedure described above, can be seen in the code segment below, in a function called Verify_CRC( ).
```java
public static int Verify_CRC(int message){ 
    int generator = 0b110101; 
    int msbbit = 0b100000; 
    int mask = 0b111111; 
    int crc = 0; 
    int messageInput = message; 
    
    for (int i = 9 + 5; i >= 5; i--){ 
        crc = (byte)(crc << 1); 
        crc = crc & mask; 
        crc = ((int)(messageInput & (1 << i)) != 0) ? 
              (int)(crc | 0x01) : (int)(crc & 0b111110); 
              
        if ((crc & msbbit) != 0) { 
            crc = (int)(crc ^ generator); 
        } 
    } 
    return crc; 
} 
```

### Results
The program run for 18 billion messages in 1 hour. 

Total messages = > 283.017.413
Corrupted messages detected by CRC => 282.799.339
Corrupted messages not detected by CRC => 218.074

Percentage of corrupted messages = 1.489%
Percentage of corrupted messages detected by CRC = 1.488 %
Percentage of corrupted messages not detected by CRC = 0.00114%
