/*
REQUIREMENTS
We need
1. Options interface
2. File handling interface
    2.1. Read
    2.2. Edit
    2.3. Write
3. File listing and searching interface
    3.1. List directory contents
        3.1.1. Current directory
        3.1.2. Recursive
    3.2. Search file
        3.2.1. Current directory
        3.2.2. Recursive
*/
/*
NOTES
An interface in Java is a blueprint of a class.  
Interface in Java is a mechanism to achieve greater abstraction.
An interface only contains public, static and final variables and/or abstract classes.
It must be implemented through classes.
*/
//FILE HANDLING MODULES...
import java.io.File; //For basic file functions
import java.io.FileNotFoundException; //For handling cases where file is not found
import java.io.FileWriter; //For writing to file
import java.util.Scanner; //For reading from file
//INPUT HANDLING MODULES...
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedInputStream;
import java.io.IOException;
//INTERFACES
interface NoteHandlerBase
{
    public void makeMainDir() throws IOException;
    public String referToMainDir(String fileName);
    public String inputFileName(String message) throws IOException;
    public boolean fileExists(String fileName, boolean displayMessage);
    public boolean fileIsDirectory(String fileName, boolean displayMessage);
}
interface DisplayedTexts
{
    public void welcome();
    public void about() throws IOException;
}
interface OptionsMenu
{
    public int basicMenu() throws IOException;
    public int advancedMenu(int basicMenuOption) throws IOException;
}
interface FileListingAndSearching
{
    String myDir = "./MyNotes/";
    public boolean searchFile(String fileName, String dirName, boolean recursion);
    public File stringToFile(String fileName);
    public void listFiles(File dir, boolean recursion);
}
interface FileHandling extends FileListingAndSearching
{
    public void writeToFile(String fileName) throws IOException;
    public void readFromFile(String fileName) throws IOException;
    public void createFile(String fileName) throws IOException;
}
//IMPLEMENTATION OF INTERFACES
abstract class NoteHandlerBaseImplementation implements NoteHandlerBase, DisplayedTexts, OptionsMenu, FileHandling
{
    public void makeMainDir() throws IOException
    {
        File dir = new File(myDir);
        dir.mkdirs();
    }
    public String referToMainDir(String fileName)
    {
        return myDir + fileName;
    }
    public String inputFileName(String message) throws IOException
    {
        String incompleteFileName, completeFileName;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(message);
        return br.readLine();
    }
    public boolean fileExists(String fileName, boolean displayMessage)
    {
        File file = new File(fileName);
        if(file.exists() == false)
        {
            if(displayMessage == true)
                System.out.println("Your file has not been found.");
            return false;
        }
        return true;
    }
    public boolean fileIsDirectory(String fileName, boolean displayMessage)
    {
        File file = new File(fileName);
        if(fileName.endsWith("/") == true || file.isDirectory() == true)
        {
            if(displayMessage == true)
                System.out.println("The file name you entered is a directory.");
            return true;
        }
        return false; 
    }
}
abstract class DisplayedTextsImplementation extends NoteHandlerBaseImplementation
{
    public void welcome()
    {
        System.out.println();
        System.out.println("NOTE TAKING PROGRAM");
        System.out.println("-------------------");
    }
    public void about() throws IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("This is a program to facilitate digital notes.");
        System.out.println("Notes are stored in " + myDir);
        System.out.println("Press enter to continue... ");
        while((char)br.read() != '\n');
    }
}
abstract class OptionsMenuImplementation extends DisplayedTextsImplementation
{
    int n;
    public int basicMenu() throws IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println();
        System.out.println("BASIC MENU");
        System.out.println("0 to exit");
        System.out.println("1 to open file");
        System.out.println("2 to search file");
        System.out.println("3 to list files");
        return Integer.parseInt(br.readLine());
    }
    public int advancedMenu(int basicMenuOption) throws IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int cases1 = 3;
        int cases2 = 2;
        if(basicMenuOption != 0)
        {
            System.out.println();
            System.out.println("ADVANCED MENU");
        }
        switch(basicMenuOption)
        {
            case 1:
                System.out.println("1 to create file");
                System.out.println("2 to edit file");
                System.out.println("3 to read file");
                return Integer.parseInt(br.readLine());
            case 2:
                System.out.println("1 to search file in " + myDir);
                System.out.println("2 to search file recursively in " + myDir);
                return Integer.parseInt(br.readLine()) + cases1;
            case 3:
                System.out.println("1 to list files in a chosen directory");
                System.out.println("2 to list files recursively in " + myDir);
                return Integer.parseInt(br.readLine()) + cases1 + cases2;
        }
        return 0;
    }
}
abstract class FileListingAndSearchingImplementation extends OptionsMenuImplementation
{
    public boolean searchFile(String fileName, String dirName, boolean recursion)
    {
        File dir = new File(dirName);
        File[] files = dir.listFiles();
        System.out.println("--Start of search in " + dir + "--");
        try 
        {
            int i = 0;
            while(true)
            {
                if(files[i].getPath().contains(fileName))
                {
                    System.out.println("Match at " + files[i]);
                }
                if(files[i].isDirectory() == true && recursion == true)
                {
                    searchFile(fileName, files[i].getPath(), recursion);
                }
                i++;
            }
        } 
        catch(Exception e) 
        {
            System.out.println("--End of search in " + dir + "--");
        }
        return false;
    }
    public File stringToFile(String fileName)
    {
        File file = new File(fileName);
        return file;
    }
    public void listFiles(File dir, boolean recursion)
    {
        File[] files = dir.listFiles();
        System.out.println("--Start of " + dir + "--");
        try 
        {
            int i = 0;
            while(true)
            {
                if(files[i].isDirectory() && recursion == true)
                {
                    listFiles(files[i++], true);
                }
                else
                {
                    System.out.println(files[i++]);
                }
            }
        } 
        catch(Exception e) 
        {
            System.out.println("--End of " + dir + "--");
        }
        return;
    }
}
abstract class FileHandlingImplementation extends FileListingAndSearchingImplementation
{
    public void writeToFile(String fileName) throws IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        FileWriter fw = new FileWriter(fileName);
        String line;
        if(fileExists(fileName, true) == false)
            return;
        if(fileIsDirectory(fileName, true) == true)
            return;
        System.out.println();
        System.out.println("--Started writing to " + fileName + "--");
        System.out.println("Type and enter <quit> to to stop writing");
        System.out.println();
        while(true)
        {
            line = br.readLine();
            if(line.equals("<quit>"))
            {
                System.out.println();
                System.out.println("--Stopped writing to " + fileName + "--");
                System.out.println();
                break;
            }
            fw.write(line);
            fw.write("\n");
        }
        fw.close();
    }
    public void readFromFile(String fileName) throws IOException, FileNotFoundException
    {
        File file = new File(fileName);
        if(fileExists(fileName, true) == false)
            return;
        if(fileIsDirectory(fileName, true) == true)
            return;
        Scanner fr = new Scanner(file);
        System.out.println();
        System.out.println("--Started reading from " + fileName + "--");
        System.out.println();
        while(fr.hasNextLine() == true)
            System.out.println(fr.nextLine());
        fr.close();
        System.out.println();
        System.out.println("--Stopped reading from " + fileName + "--");
        System.out.println();
    }
    public void createFile(String fileName) throws IOException
    {
        File file = new File(fileName);
        if(file.createNewFile() == false)
        {
                System.out.println("A file named " + fileName + " has been found.");
        }
        else
        {
            if(fileIsDirectory(fileName, false) == false)
            {
                writeToFile(fileName);
            }
            else
            {
                if(file.mkdirs() == true)
                    System.out.println("Directory created.");
                else
                    System.out.println("Directory with name " + fileName + " exists in the working directory.");
            }
        }
    }
}
class NoteHandler extends FileHandlingImplementation
{
    NoteHandler(){}
}
class NoteTaking
{
    public static void main(String args[]) throws IOException
    {
        int i = 1;
        String fileName;
        NoteHandler nh = new NoteHandler();
        nh.welcome();
        nh.about();
        nh.makeMainDir();
        do
        {
            i = nh.advancedMenu(nh.basicMenu());
            switch(i)
            {
                case 1: nh.createFile(nh.referToMainDir(nh.inputFileName("Enter your file name: "))); break;
                case 2: nh.writeToFile(nh.referToMainDir(nh.inputFileName("Enter the file name: "))); break;
                case 3: nh.readFromFile(nh.referToMainDir(nh.inputFileName("Enter the file name: "))); break;
                case 4: nh.searchFile(nh.inputFileName("Enter the string you want to search: "), nh.referToMainDir(nh.inputFileName("Enter the directory you want to search in: ")), false); break;
                case 5: nh.searchFile(nh.inputFileName("Enter the file name: "), nh.myDir, true); break;
                case 6: nh.listFiles(nh.stringToFile(nh.inputFileName("Enter the directory name: ")), false); break;
                case 7: nh.listFiles(nh.stringToFile(nh.myDir), true); break;
            }
        } while(i != 0);
    }
}