public class parity {

    public static void main(String[] args) {

        //14 bit message
        int count = 0;

        long message14 = 10001101011000L;

        //28 bit message in hex representation
        long message28a = 0b1100111;
        long message28b = 0b1011101;
        long message28c = 0b0111001;
        long message28d = 0b0101001;
        
        int parBit;

        // calculation of parity bit 
        parBit = parityBit(message14);

        message14 = message14 << 1 | parBit;

        // distorted messages
        long m1 = 100011010110000L;
        long m2 = 100010010110000L;
        long m3 = 100010010111000L;
        
        // parity bits for distorted messages
        boolean par1, par2, par3;
        par1 = isParityEven(m1);
        par2 = isParityEven(m2);
        par3 = isParityEven(m3);

        // results
        System.out.println("Message with no errors is detectes as: " + par1);
        System.out.println("Message with 1 error is detectes as: " + par2);
        System.out.println("Message with 2 errors is detectes as: " + par3);
        System.out.println();

        par1 = dimensionalParity(message28a, message28b, message28c, message28d);
        par2 = dimensionalParity(message28a, 0b1001101, message28c, message28d);
        par3 = dimensionalParity(message28a, 0b1001001, message28c, message28d);

        System.out.println("Matrix with no errors is detectes as: " + par1);
        System.out.println("Matrix with 1 error is detectes as: " + par2);
        System.out.println("Matrix with 2 errors is detectes as: " + par3);
        System.out.println();
    }

    //function to create 2 dimensional table with messages and check the errors 
    public static boolean dimensionalParity(long message28a, long message28b, long message28c, long message28d) {

        int count;
        long bit = 0;
        int mask = 0b1000000;

        long[][] parityArray = new long[5][8];
        
        // create table
        for(int j = 0; j < 7; j++){
            parityArray[0][j] = message28a & mask;
            parityArray[1][j] = message28b & mask;
            parityArray[2][j] = message28c & mask;
            parityArray[3][j] = message28d & mask;
            mask = mask >> 1;
        }

        // calculate row parities
        parityArray[0][7] = parityBit(message28a);
        parityArray[1][7] = parityBit(message28b);
        parityArray[2][7] = parityBit(message28c);
        parityArray[3][7] = parityBit(message28d);
        
        
        // calculate column parities
        for(int i = 0; i < 8; i++){
            count = 0;
            for(int j = 0; j < 4; j++){
                if(parityArray[j][i] != 0){
                    count++;
                }
            }

            if(count % 2 == 0){
                parityArray[4][i] = 0;
            }
            else{
                parityArray[4][i] = 1;
            }

        }

        count = 0;
        
        // calculate parity for the row parities column
        for(int j = 0; j < 7; j++){
            if(parityArray[4][j] != 0){
                count++;
            }
        }
        
        if(count % 2 == 0){
            bit = 0;
        }
        else{
            bit = 1;
        }

        // check if row parity of column parities == column parity of row parities
        if (bit == parityArray[4][7]){
            return true;
        }
        else{
            return false;
        }
    }

    // calculate parity bit
    public static int parityBit(long message14) {
        int parBit;
        boolean par = isParityEven(message14);

        if(!par){
            parBit = 0;
        }
        else{
            parBit = 1;
        }
        return parBit;
    }

    // check if parity is even
    public static boolean isParityEven(long message){
        boolean parity = false;

        while(message != 0){
            parity = !parity;
            message = message & (message - 1);
        }
        return parity;
    }
}