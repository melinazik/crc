import java.io.*;
import java.util.*;

public class CRC {

    public static void main(String[] args) {

        long totalErrCRC = 0;
        long totalNotDetected = 0;
        long totalMessages = 0;
        long totalBitFlips = 0;

        try (PrintWriter writer = new PrintWriter(new File("test.csv"))) {

            StringBuilder sb = new StringBuilder();

            //csv column title format
            sb.append("TotalMessages,CRCErr,NotDetected,TotalBits,BitFlips,BER\n");
            writer.write(sb.toString());
            sb.setLength(0);


            Random r = new Random();
            for (long i = 0; i <  1000000000000000L; i++) { // 1 quadrillion test messages
                totalMessages++;

                //0x400 is equal to a 10 bit number
                int message = r.nextInt(0x400);
                
                // compute crc for message
                int crc = Compute_CRC(message);
                
                // shift message by 5 bits (as long as the crc)
                // and the crc at the end
                int crcMessage = message << 5 | crc;

                int transmittedMessage = crcMessage;
                
                int bitflip = 0;


                //calculate the BER probability for each bit
                // in the message and then decide if it's going
                // to be distorted

                // the probability is 1/1000 as asked by the project announcement

                for (int j = 0; j < 15; j++) {
                    // simulate BER
                    if (r.nextInt(1000) < 1) {  // 1/1000 BER
                        
                        int bit = r.nextInt(15);
                        
                        // XOR bit
                        transmittedMessage = transmittedMessage ^ (1 << bit);
                        bitflip++;
                        totalBitFlips++;
                    }
                }

                // keep the crc of the distorted message
                // with the use of a 5-bit long mask
                int crcTrans = transmittedMessage & 0b11111;

                // check if the crc's before and after distortion match
                int verify = Verify_CRC(transmittedMessage);

                // if crcs dont match, it has an error
                if (verify != crcTrans) {
                    totalErrCRC++;
                } 

                // if there are changed bits,
                // then there is an undetected error 
                else if (bitflip > 0) {
                    totalNotDetected++;
                }


                // print in the csv file the following number for ever 10M messages
                if (totalMessages % 10000000 == 0) { // 10 million
                 
                    sb.append(totalMessages);
                    sb.append(",");
                    sb.append(totalErrCRC);
                    sb.append(",");
                    sb.append(totalNotDetected);
                    sb.append(",");
                    sb.append(totalMessages * 15L);
                    sb.append(",");
                    sb.append(totalBitFlips);
                    sb.append(",");
                    sb.append(totalBitFlips / (15.0 * totalMessages));
                    sb.append("\n");

                    writer.write(sb.toString());
                    sb.setLength(0);
                    writer.flush();
                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        // results
        long totalErrMsgs = totalNotDetected + totalErrCRC;
        System.out.println("Total messages: " + totalMessages);
        System.out.println("Total messages with errors: " + totalErrMsgs);
        System.out.println("Total errors detected: " + totalErrCRC);
        System.out.println("Total errors not detected: " + totalNotDetected);
        System.out.println();
        System.out.println("Total bits flipped: " + totalBitFlips);
        System.out.printf("BER: %.6f\n", totalBitFlips / (15.0 * totalMessages));
    }

    // compute the crc of a message (10 bit)
    public static int Compute_CRC(int message)
    {
        int generator = 0b110101; //0x35
        int msbbit =    0b10000; //0x20 -- most significant bit 
        int mask =      0b111111; //0x3F
        int crc = 0; //init crc register with 0

        int messageInput = message;

        //handle each bit of input by iterating over each bit
        for (int i = 9; i >= 0; i--){
            
            //check if MSB is set
            if ((crc & msbbit) != 0){   
                
                // if MSB set, shift it out of the crc
                crc = (byte)(crc << 1);
                crc = crc & mask;

                /* shift in next bit of input stream:
                 * If it's 1, set LSB of crc to 1.
                 * If it's 0, set LSB of crc to 0. */
                crc = ((int)(messageInput & (1 << i)) != 0) ? (int)(crc | 0x01) : (int)(crc & 0b111110);

                //Perform the 'division' by XORing the crc register with the generator polynomial
                crc = (int)(crc ^ generator);
            }
            else{   
                // if MSB not set, shift it out and shift in next bit of input stream
                crc = (int)(crc << 1);
                crc = crc & mask;
                crc = ((int)(messageInput & (1 << i)) != 0) ? (int)(crc | 0x01) : (int)(crc & 0b111110);
            }
        }

        return crc;
    }

    // compute the crc of a mesage with crc (15 bit)
    public static int Verify_CRC(int message){
        
        int generator = 0b110101; //0x35
        int msbbit =    0b100000; //0x20
        int mask =      0b111111; //0x3F
        int crc = 0; //init crc register with 0

        int messageInput = message;

        //handle each bit of input by iterating over each bit
        for (int i = 9 + 5; i >= 5; i--){
            
            crc = (byte)(crc << 1);
            crc = crc & mask;

            /* shift in next bit of input stream:
             * If it's 1, set LSB of crc to 1.
             * If it's 0, set LSB of crc to 0. */
            crc = ((int)(messageInput & (1 << i)) != 0) ? (int)(crc | 0x01) : (int)(crc & 0b111110);

            // check if MSB is set
            if ((crc & msbbit) != 0)
            {
                // Perform the 'division' by XORing the crc register with the generator polynomial
                crc = (int)(crc ^ generator);
            }
        }

        return crc;
    }
}