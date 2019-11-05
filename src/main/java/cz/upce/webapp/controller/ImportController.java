package cz.upce.webapp.controller;

import java.io.IOException;
import java.util.Objects;

import cz.upce.webapp.dao.stock.repository.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cz.upce.webapp.service.ItemServiceImpl;
import cz.upce.webapp.utils.FileManipulator;
import cz.upce.webapp.utils.xlsprocessors.CountrySheetProcessor;
import cz.upce.webapp.utils.xlsprocessors.ISheetProcessor;
import cz.upce.webapp.utils.xlsprocessors.NutSheetProcessor;

/**
 * Class used to importItemsFromFile the .xls or .xlsx file
 *
 * @author Tomas Kodym
 */
@Controller
public class ImportController
{
    private static final String UPLOADING_DIR = System.getProperty("user.dir") + "/uploadingDir/";

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportController.class);

    @Autowired
    private ItemServiceImpl itemServiceImpl;

    @Autowired
    NutSheetProcessor nutSheetProcessor;

    @Autowired
    CountrySheetProcessor countrySheetProcessor;

    @Autowired
    private ItemRepository itemRepository;

    @PostMapping("/upload")
    public String singleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("cmb_value") String value, RedirectAttributes redirectAttributes)
    {
        if (file.isEmpty())
        {
            redirectAttributes.addFlashAttribute("message", "No file for import was specified!");
            LOGGER.warn("No file for import was specified!");
            return "redirect:/notifier";
        }

        if (!Objects.requireNonNull(file.getOriginalFilename()).contains(".xls") && !file.getOriginalFilename().contains(".xlsx"))
        {
            redirectAttributes.addFlashAttribute("message", "File with content type: " + file.getContentType() + "is not supported!");
            LOGGER.warn("File with content type: " + file.getContentType() + "is not supported!");
            return "redirect:/notifier";
        }

        try
        {
            //before import, remove all previous data according to the supplier
            itemServiceImpl.deleteAllBySupplier(value);

            FileManipulator.saveFile(file, UPLOADING_DIR);
            redirectAttributes.addFlashAttribute("message", "File was successfully uploaded!");
            //select processor aligned with combobox value
            ISheetProcessor sheetProcessor = selectProcessor(value);

            sheetProcessor.importItemsFromFile(file, UPLOADING_DIR, itemRepository);

            FileManipulator.deleteFile(file, UPLOADING_DIR);

        }
        catch (IOException e)
        {
            redirectAttributes.addFlashAttribute("message", "There was an error during the file import!");
            LOGGER.error("File: " + file.getName() + " could not been save to local file system because of: ", e);
        }

        return "redirect:/notifier";
    }

    @GetMapping("/notifier")
    public String uploadStatus()
    {
        return "importer/notifier";
    }

    private ISheetProcessor selectProcessor(String selectedProcessor)
    {
        switch (selectedProcessor)
        {
            case "Oříšek s.r.o.":
                LOGGER.info("Selecting Orisek processor for parsing the documents!");
                return nutSheetProcessor;
            case "Country life":
                LOGGER.info("Selecting Country processor for parsing the documents!");
                return countrySheetProcessor;
            default:
                LOGGER.info("Selecting Default processor for parsing the documents!");
                return nutSheetProcessor;
        }
    }
}
