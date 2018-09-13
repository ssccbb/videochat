package com.weiliao.kinnek.utils;

import java.io.ByteArrayOutputStream;

/**
 * Base64转换实现类
 */
public class Base64 {

	private static byte[] encodingTable = {
        (byte) 'A', (byte) 'B', (byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F', (byte) 'G',
        (byte) 'H', (byte) 'I', (byte) 'J', (byte) 'K', (byte) 'L', (byte) 'M', (byte) 'N',
        (byte) 'O', (byte) 'P', (byte) 'Q', (byte) 'R', (byte) 'S', (byte) 'T', (byte) 'U',
        (byte) 'V', (byte) 'W', (byte) 'X', (byte) 'Y', (byte) 'Z', (byte) 'a', (byte) 'b',
        (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f', (byte) 'g', (byte) 'h', (byte) 'i',
        (byte) 'j', (byte) 'k', (byte) 'l', (byte) 'm', (byte) 'n', (byte) 'o', (byte) 'p',
        (byte) 'q', (byte) 'r', (byte) 's', (byte) 't', (byte) 'u', (byte) 'v', (byte) 'w',
        (byte) 'x', (byte) 'y', (byte) 'z', (byte) '0', (byte) '1', (byte) '2', (byte) '3',
        (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9', (byte) '+',
        (byte) '/'
    };
	
	/**
	 * 二进制数据编码转换为Base64字符串
	 * @param bData 二进制数据
	 * @return 返回转换为Base64的字符串
	 */
	public static byte[] encode(byte[] bData) {
		byte[] value = null;
		try {
			value = encode(bData, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * Base64字符串解码为二进制数据
	 * @return 返回经Base64转换后数据
	 */
	public static byte[] decode(byte[] indata){
		byte[] bytes = null;
        byte b1;
        byte b2;
        byte b3;
        byte b4;

		try {
			if ((indata == null) || (indata.length < 1)) {
	            return null;
	        }
			
			ByteArrayOutputStream os = new ByteArrayOutputStream();

	        // clean the inputdata
	        int k = 0;

	        do {
	            if ((indata[k] != '\n') && (indata[k] != '\r')) {
	                os.write(indata[k]);
	            }

	            k++;
	        } while (k < indata.length);

	        // now start decoding
	        byte[] data = os.toByteArray();

	        if (data[data.length - 2] == '=') {
	            bytes = new byte[(((data.length / 4) - 1) * 3) + 1];
	        } else if (data[data.length - 1] == '=') {
	            bytes = new byte[(((data.length / 4) - 1) * 3) + 2];
	        } else {
	            bytes = new byte[((data.length / 4) * 3)];
	        }

	        for (int i = 0, j = 0; i < (data.length - 4); i += 4, j += 3) {
	            b1 = decodingTable[data[i]];
	            b2 = decodingTable[data[i + 1]];
	            b3 = decodingTable[data[i + 2]];
	            b4 = decodingTable[data[i + 3]];

	            bytes[j] = (byte) ((b1 << 2) | (b2 >> 4));
	            bytes[j + 1] = (byte) ((b2 << 4) | (b3 >> 2));
	            bytes[j + 2] = (byte) ((b3 << 6) | b4);
	        }

	        if (data[data.length - 2] == '=') {
	            b1 = decodingTable[data[data.length - 4]];
	            b2 = decodingTable[data[data.length - 3]];

	            bytes[bytes.length - 1] = (byte) ((b1 << 2) | (b2 >> 4));
	        } else if (data[data.length - 1] == '=') {
	            b1 = decodingTable[data[data.length - 4]];
	            b2 = decodingTable[data[data.length - 3]];
	            b3 = decodingTable[data[data.length - 2]];

	            bytes[bytes.length - 2] = (byte) ((b1 << 2) | (b2 >> 4));
	            bytes[bytes.length - 1] = (byte) ((b2 << 4) | (b3 >> 2));
	        } else {
	            b1 = decodingTable[data[data.length - 4]];
	            b2 = decodingTable[data[data.length - 3]];
	            b3 = decodingTable[data[data.length - 2]];
	            b4 = decodingTable[data[data.length - 1]];

	            bytes[bytes.length - 3] = (byte) ((b1 << 2) | (b2 >> 4));
	            bytes[bytes.length - 2] = (byte) ((b2 << 4) | (b3 >> 2));
	            bytes[bytes.length - 1] = (byte) ((b3 << 6) | b4);
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}

        return bytes;
	}
	
	/**
     * encode the input data producong a base 64 encoded byte array.
     * @param data the data to be encoded
     * @param splitlines whether the output lines will be split by '\n' (64 byte rows) or not.
     * @return a byte array containing the base 64 encoded data.
     */
    public static byte[] encode(byte[] data, boolean splitlines) {
        byte[] bytes;

        if ((data.length % 3) == 0) {
            bytes = new byte[(4 * data.length) / 3];
        } else {
            bytes = new byte[4 * ((data.length / 3) + 1)];
        }

        for (int i = 0, j = 0; i < ((data.length / 3) * 3); i += 3, j += 4) {
            int b1;
            int b2;
            int b3;
            int b4;
            int d1;
            int d2;
            int d3;

            d1 = data[i] & 0xff;
            d2 = data[i + 1] & 0xff;
            d3 = data[i + 2] & 0xff;

            b1 = (d1 >>> 2) & 0x3f;
            b2 = ((d1 << 4) | (d2 >>> 4)) & 0x3f;
            b3 = ((d2 << 2) | (d3 >>> 6)) & 0x3f;
            b4 = d3 & 0x3f;

            bytes[j] = encodingTable[b1];
            bytes[j + 1] = encodingTable[b2];
            bytes[j + 2] = encodingTable[b3];
            bytes[j + 3] = encodingTable[b4];
        }

        /*
         * process the tail end.
         */
        int b1;

        /*
         * process the tail end.
         */
        int b2;

        /*
         * process the tail end.
         */
        int b3;
        int d1;
        int d2;

        switch (data.length % 3) {
        case 0: /* nothing left to do */
            break;

        case 1:
            d1 = data[data.length - 1] & 0xff;
            b1 = (d1 >>> 2) & 0x3f;
            b2 = (d1 << 4) & 0x3f;

            bytes[bytes.length - 4] = encodingTable[b1];
            bytes[bytes.length - 3] = encodingTable[b2];
            bytes[bytes.length - 2] = (byte) '=';
            bytes[bytes.length - 1] = (byte) '=';

            break;

        case 2:
            d1 = data[data.length - 2] & 0xff;
            d2 = data[data.length - 1] & 0xff;

            b1 = (d1 >>> 2) & 0x3f;
            b2 = ((d1 << 4) | (d2 >>> 4)) & 0x3f;
            b3 = (d2 << 2) & 0x3f;

            bytes[bytes.length - 4] = encodingTable[b1];
            bytes[bytes.length - 3] = encodingTable[b2];
            bytes[bytes.length - 2] = encodingTable[b3];
            bytes[bytes.length - 1] = (byte) '=';

            break;
        }

        if (splitlines == false) {
            return bytes;
        }

        // make sure we get limited lines...
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
        	for (int i = 0; i < bytes.length; i += 64) {
                if ((i + 64) < bytes.length) {
                    os.write(bytes, i, 64);
                    os.write('\n');
                } else {
                    os.write(bytes, i, bytes.length - i);
                }
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
        

        return os.toByteArray();
    }
    
    /*
     * set up the decoding table.
     */
    private static byte[] decodingTable;

    static {
        decodingTable = new byte[128];

        for (int i = 'A'; i <= 'Z'; i++) {
            decodingTable[i] = (byte) (i - 'A');
        }

        for (int i = 'a'; i <= 'z'; i++) {
            decodingTable[i] = (byte) (i - 'a' + 26);
        }

        for (int i = '0'; i <= '9'; i++) {
            decodingTable[i] = (byte) (i - '0' + 52);
        }

        decodingTable['+'] = 62;
        decodingTable['/'] = 63;
    }
}