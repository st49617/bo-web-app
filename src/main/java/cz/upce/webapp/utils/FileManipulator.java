package cz.upce.webapp.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author Tomas Kodym
 */

public class FileManipulator
{
    private static final Logger LOGGER = LoggerFactory.getLogger(FileManipulator.class);

    private static void createFile(String fileName)
    {
        File file = new File(fileName);
        if (!file.exists())
        {
            //If directory not exists, then create
            if (file.getParentFile() != null)
            {
                file.getParentFile().mkdirs();
            }

            try
            {
                if (file.createNewFile())
                    LOGGER.info("File " + file.getAbsolutePath() + " successfully created.");
            }
            catch (IOException e)
            {
                LOGGER.error("Creating of file " + fileName + "failed", e);
            }
        }
    }

    public static void deleteFile(MultipartFile file, String fileToSave)
    {
        Path path = Paths.get(fileToSave + File.separator + file.getOriginalFilename());
        if (Files.exists(path))
        {
            try
            {
                Files.delete(path);
                LOGGER.info("File: " + path.getFileName() + " was successfully deleted!");
            }
            catch (IOException e)
            {
                LOGGER.error("It was unable to delete the file" + path.toString() + "!");
            }
        }
        else
        {
            LOGGER.warn("File:" + path.getFileName() + "was not found!");
        }
    }

    public static void saveFile(MultipartFile file, String fileToSave) throws IOException
    {
        byte[] bytes = file.getBytes();
        Path path = Paths.get(fileToSave + File.separator + file.getOriginalFilename());
        if (Files.exists(path))
        {
            Files.write(path, bytes);
        }
        else
        {
            FileManipulator.createFile(path.toString());
            Files.write(path, bytes);
        }

        LOGGER.info("File: " + file.getOriginalFilename() + " was successfully writen into path!");
    }
}