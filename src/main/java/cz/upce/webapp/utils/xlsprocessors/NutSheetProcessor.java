package cz.upce.webapp.utils.xlsprocessors;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cz.upce.webapp.dao.stock.model.Item;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

/**
 * @author Tomas Kodym
 */

@Component
public class NutSheetProcessor extends AbstractSheetProcessor
{
    @Override
    public Integer supplerId() {
        return 1;
    }

    @Override
    public List<Item> disintegrateIntoItem(int rowIdx, List<String> rowData) {
        if (!rowData.isEmpty())
        {
            String[] values = rowData.toArray(new String[0]);
            List<Item> itemsList = new ArrayList<>();
            //split values from list to array
            //for two columns in excel we need to parse both
            if (values.length >= 3)
            {
                parseOneItem(values, itemsList, 0);
            }
            if (values.length >= 9)
            {
                parseOneItem(values, itemsList, 5);
            }
            return itemsList;
        }
        else {
            return new ArrayList<>();
        }
    }

    private void parseOneItem(String[] values, List<Item> itemsList, int startColumnIdx) {
        String quantityStr = values[startColumnIdx + 1];
        if (StringUtils.isNumeric(quantityStr)) {
            String itemName = values[startColumnIdx + 0];
            double itemQuantity = countKilosToGrams(Double.parseDouble(quantityStr));
            double itemPrice = Double.parseDouble(values[startColumnIdx + 2])/1000;
            int itemTax = Integer.parseInt(values[startColumnIdx + 3]);
            itemsList.add(new Item(itemName, itemQuantity, itemPrice, itemTax, null));
        } else
            LOGGER.warn("Item was not created, because of non numeric value in quantity column!");
    }

    private double countKilosToGrams(Double kilos)
    {
        return kilos * 1000;
    }

    @Override
    public Sheet getOrderSheetFromWorkbook(Workbook workbook) {
        return super.getOrderSheetFromWorkbook(workbook).getWorkbook().getSheet("Objednávka");
    }

    public int getOrderColumnIdx() {
        return 5;
    }

    @Override
    public void setOrderQuantityForItem(Sheet orderSheet, Item item, Integer orderQuantity) {
        int rowIdx = 3;
        boolean finish = false;
        // We find item based on its name
        while (!finish) {
            Row row = orderSheet.getRow(rowIdx);
            finish = (row == null);
            if (!finish) {
                String name = row.getCell(1).getStringCellValue();
                if (name!=null && name.equals(item.getItemName())) {
                    row.createCell(getOrderColumnIdx()).setCellValue(orderQuantity);
                    finish=true;
                }
            }
            rowIdx++;
        }
    }
}
