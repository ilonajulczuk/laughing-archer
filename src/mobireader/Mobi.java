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
import unzipping.CustomLZ77;


public class Mobi {
    File file;
    public String contents;
    public Header header;
    public PalmDocHeader palmdocHeader;
	public MobiHeader mobiHeader;
	EXTHHeader exthHeader;
    public HeadersUtil headers = new HeadersUtil();
    public int numberOfRecords;
    ArrayList<RecordInfo> recordsInfo;
    int currentOffset = 0;
    
    public Mobi(String filename)
    {
      try {
        this.file = new File(filename);
      }
      catch (IOError e) {
        throw e; 
      }
    }
    
    public void parse() throws IOException, DecodingException
    {
        byte  compressed [] = Files.toByteArray(this.file);
        this.contents = new String(compressed);
        this.header = parseHeader();
        this.recordsInfo = parseRecordInfoList(this.file, currentOffset);
        this.readRecord0();
    }
    
    public PalmDocHeader parsePalmDOCHeader(File file, int offset) throws IOException, DecodingException {
    	Codec<PalmDocHeader> codec = Codecs.create(PalmDocHeader.class);
		RandomAccessFile fp = new RandomAccessFile(file, "r");
		fp.seek(offset);
		byte[] buff= new  byte[headers.palmDocHeaderSize];
		fp.read(buff, 0, headers.palmDocHeaderSize);
		PalmDocHeader palmDocHeader = Codecs.decode(codec, buff);
        currentOffset += headers.palmDocHeaderSize;
        fp.close();
        return palmDocHeader;
    }
    
    public MobiHeader parseMobiHeader(File file, int offset) throws IOException, DecodingException {
    	Codec<MobiHeader> codec = Codecs.create(MobiHeader.class);
		RandomAccessFile fp = new RandomAccessFile(file, "r");
		fp.seek(offset);
		byte[] buff= new  byte[headers.mobiHeaderSize];
		fp.read(buff, 0, headers.mobiHeaderSize);
		MobiHeader mobiHeader = Codecs.decode(codec, buff);
        currentOffset += headers.mobiHeaderSize;
        fp.close();
        return mobiHeader;
    }
    
    public EXTHHeader parseEXTHHeader(File file, int offset) throws IOException, DecodingException{
    	Codec<EXTHHeader> codec = Codecs.create(EXTHHeader.class);
		RandomAccessFile fp = new RandomAccessFile(file, "r");
		fp.seek(offset);
		byte[] buff= new  byte[headers.exthHeaderSize];
		fp.read(buff, 0, headers.exthHeaderSize);
		EXTHHeader exthHeader = Codecs.decode(codec, buff);
        currentOffset += headers.mobiHeaderSize;
        fp.close();
        return exthHeader;
    }
    
  public void readRecord0() throws DecodingException, IOException {
	  currentOffset += 2; //Gap to data
	  palmdocHeader = parsePalmDOCHeader(file, currentOffset);
	  mobiHeader = parseMobiHeader(file, currentOffset);
	  exthHeader = null;
	  if(mobiHeader.hasEXTHHeader()) {
		  exthHeader = parseEXTHHeader(file, currentOffset);
	  }
  }
  
  public Header parseHeader() throws FileNotFoundException, DecodingException, IOException{           
        Header parsedHeader =  createHeaderBasedOn(file);
        return parsedHeader;
  }
  
  public Header createHeaderBasedOn(File file) throws FileNotFoundException, DecodingException, IOException 
  {
      Codec<Header> codec = Codecs.create(Header.class);
      Header headerFromText = Codecs.decode(codec, file);
      currentOffset += headers.headerSize;
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
		  byte[] buff = new  byte[len];
		  fp.read(buff, 0, len);
          RecordInfo recordInfo = Codecs.decode(codec, buff);
          assert recordInfo != null: "Entry " + String.valueOf(i) + " was null";
          recordsInfo.add(recordInfo);
          myoffset += 8;
          fp.close();
	  }
	 currentOffset = myoffset;
	 return recordsInfo;
  }
  
  public byte[] readRawRecord(int index) throws IOException {
	  int offset = (int)recordsInfo.get(index).recordDataOffset;
	  int len;
	  if(index + 1 < recordsInfo.size()) {
	   len = (int)recordsInfo.get(index + 1).recordDataOffset - offset;
	  }
	  else len = 4096;
      RandomAccessFile raf = new RandomAccessFile(file, "rw");
      byte [] buff = new  byte[len];
      raf.seek(offset);
      raf.read(buff, 0, len);
      raf.close();
      return buff;
  }
  
  public String readRecord(int index, boolean disable_compression) throws IOException {
    
	  byte[] rawRecord = readRawRecord(index);
      if (palmdocHeader.Compression == 1 || disable_compression) {
    	  return new String(rawRecord);
      }
      else if(palmdocHeader.Compression == 2)
      {
    	  CustomLZ77 lz = new CustomLZ77();
          return lz.decompress(rawRecord);	
      }
      else {
    	  return "Unknown compression type, raw: " + rawRecord;
      }
  }
  
  public byte[] readImageRecord(int imgnum) throws IOException {
	  long recordnum = mobiHeader.firstImageIndex + imgnum;
      return readRawRecord((int)recordnum);
  }
  
  public String readAllRecords() throws IOException {
	  String all = "";
	  long upperBound =  palmdocHeader.recordCount;
	  System.out.println("The biggest record is: " + upperBound);
	  for(int i = 1; i <= upperBound; i++)
	  {
		  all += readRecord(i, false);
	  }
	  return all;
  }
  
 /*
  String author():
    "Returns the author of the book"
    return exthHeader.records

  String title():
    "Returns the title of the book"
    return mobiHeader.['Full Name']
     */
}
