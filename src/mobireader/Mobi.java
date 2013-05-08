/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mobireader;

import com.google.common.io.Files;
import java.io.File;

import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.IOException;
import java.io.RandomAccessFile;

import nl.flotsam.preon.Codec;
import nl.flotsam.preon.Codecs;
import nl.flotsam.preon.DecodingException;

import java.util.ArrayList;
/**
 *
 * @author att
 */



public class Mobi {
    File file;
    int offset = 0;
    public String contents;
    public Header header;
    PalmDocHeader palmdocHeader;
	MobiHeader mobiHeader;
	EXTHHeader exthHeader;
    String headerFormat = "32shhIIIIII4s4sIIH";
    public int headerSize = calcsize(headerFormat);
    String palmDocHeaderFormat = ">HHIHHHH";
    public int palmDocHeaderSize = calcsize(palmDocHeaderFormat);
    public int numberOfRecords;
    ArrayList<RecordInfo> recordsInfo;
    int currentOffset = 0;
    public void setNumberOfRecords(int number) {
    	numberOfRecords = number;
    }
    public Mobi(String filename)
    {
      try {
        this.file = new File(filename);
      }
      catch (IOError e) {
        throw e;
      }
    }
    
    void parse() throws IOException, DecodingException
    {
        byte  compressed [] = Files.toByteArray(this.file);
        this.contents = new String(compressed);
        this.header = parseHeader();
        this.recordsInfo = parseRecordInfoList(this.file, 123);
        this.readRecord0();
    }
    
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
    
    public PalmDocHeader parsePalmDOCHeader(File file, int offset) throws IOException, DecodingException {
    	Codec<PalmDocHeader> codec = Codecs.create(PalmDocHeader.class);
		RandomAccessFile fp = new RandomAccessFile(file, "r");
		fp.seek(currentOffset);
		byte[] buff= new  byte[palmDocHeaderSize];
		fp.read(buff, 0, palmDocHeaderSize);
		PalmDocHeader palmDocHeader = Codecs.decode(codec, buff);
        currentOffset += palmDocHeaderSize;
        fp.close();
        return palmDocHeader;
    }
    
    public MobiHeader parseMobiHeader(File file, int offset) {
    	
    }
    
    public EXTHHeader parseEXTHHeader(File file, int offset) {
    	
    }
    
  public void readRecord0() throws DecodingException, IOException {
	  palmdocHeader = parsePalmDOCHeader(file, currentOffset);
	  mobiHeader = parseMobiHeader(file, currentOffset);
	  exthHeader = null;
	  if(mobiHeader.hasEXTHHeader()) {
		  exthHeader = parseEXTHHeader(file, currentOffset);
	  }

  }
  
  public Header parseHeader(){           
        Header parsedHeader =  createHeaderBasedOn(file);
        return parsedHeader;
  }
  
  public Header createHeaderBasedOn(File file) 
  {
      Header headerFromText;
      try {
          Codec<Header> codec = Codecs.create(Header.class);
          headerFromText = Codecs.decode(codec, file);
      }
      catch( IOException e ){
          System.out.println(e.getCause());
          headerFromText = new Header();
      }
      catch (DecodingException e)
      {
          System.out.println(e.getCause());
          headerFromText = new Header();
      }
      return headerFromText;
  }
  
  public ArrayList<RecordInfo> parseRecordInfoList(File file, int offset) throws FileNotFoundException, DecodingException, IOException
  {
	  ArrayList<RecordInfo> recordsInfo = new ArrayList<>();
	  int myoffset = offset;
	  int len = 8;
	  for(int i = 0; i < header.numberOfRecords; i++)
	  {
		  Codec<RecordInfo> codec = Codecs.create(RecordInfo.class);
		  RandomAccessFile fp = new RandomAccessFile(file, "r");
		  fp.seek(myoffset);
		  byte[] buff= new  byte[len];
		  fp.read(buff, 0, len);
          RecordInfo recordInfo = Codecs.decode(codec, buff);
          assert recordInfo != null: "Entry " + String.valueOf(i) + " was null";
          recordsInfo.add(recordInfo);
          myoffset += 8;
          fp.close();
	  }
	 return recordsInfo;
  }
    /*
  def readRecord(self, recordnum, disable_compression=False):
    if self.config:
      if self.config['palmdoc']['Compression'] == 1 or disable_compression:
        return self.contents[self.records[recordnum]['record Data Offset']:self.records[recordnum+1]['record Data Offset']];
      elif self.config['palmdoc']['Compression'] == 2:
        result = uncompress_lz77(self.contents[self.records[recordnum]['record Data Offset']:self.records[recordnum+1]['record Data Offset']-self.config['mobi']['extra bytes']])
        return result

  def readImageRecord(self, imgnum):
    if self.config:
      recordnum = self.config['mobi']['First Image index'] + imgnum;
      return self.readRecord(recordnum, disable_compression=True);

  def author(self):
    "Returns the author of the book"
    return self.config['exth']['records'][100]

  def title(self):
    "Returns the title of the book"
    return self.config['mobi']['Full Name']
     */
}
