package mobireader;

public class HeadersUtil {
	
	String headerFormat = "32shhIIIIII4s4sIIH";
    public int headerSize = calcsize(headerFormat);
    String palmDocHeaderFormat = "HHIHHHH";
    public int palmDocHeaderSize = calcsize(palmDocHeaderFormat);
    String mobiHeaderFormat = "IIIIII40sIIIIIIIIIIIII36sIIII8sHHIIIII";
    public int mobiHeaderSize = calcsize(mobiHeaderFormat);
    String exthHeaderFormat = "III";
    public int exthHeaderSize = calcsize(exthHeaderFormat);	
    
    public int calcsize(String headerFormat)
    {
        int size = 0;
        boolean is_number = false;
        String number = "";
        for(int i = 0; i < headerFormat.length(); i++)
        {
            char c = headerFormat.charAt(i);
            if(Character.isDigit(c)) 
            {
                number += c;
                is_number = true;
            }
            else if (is_number){
                size += addNumberOfBytes(Integer.parseInt(number), c);
                is_number = false;
                number = "";
            }
            else {
                size += addNumberOfBytes(1, c);
            }
        }
        return size;
    }
    
    int addNumberOfBytes(int n, char c)
    {
        int base;
 
        switch (c) {
            case 'c':  base = 1;
                     break;
            case 's':  base = 1;
                     break;
            case 'b':  base = 1;
                     break;
            case 'h':  base = 2;
                     break;
            case 'H':  base = 2;
                     break;
            case 'i':  base = 4;
                     break;
            case 'I':  base = 4;
                     break;
            case 'l':  base = 4;
                     break;
            case 'L':  base = 4;
                     break;
            case 'f':  base = 4;
                     break;
            case 'd':  base = 8;
                     break;
            default: base = 0;
                     break;
        }
        return base * n;
    }
}
