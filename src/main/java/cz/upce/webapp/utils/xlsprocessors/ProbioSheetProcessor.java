package cz.upce.webapp.utils.xlsprocessors;

import cz.upce.webapp.dao.stock.model.Item;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tomas Kodym
 */

@Component
public class ProbioSheetProcessor extends AbstractSheetProcessor
{
    @Override
    public String getSheetName() {
        return "GASTRO+ BEZOBALU";
    }

    @Override
    public Integer supplerId() {
        return 3;
    }

    @Override
    public List<Item> disintegrateIntoItem(int rowIdx, List<String> sheetData) {
        List<Item> itemsList = new ArrayList<>();
        //split values from list to array
        String[] values = sheetData.toArray(new String[0]);
        if (values.length>8) {
            if (StringUtils.isNumeric(values[8])) {
                String itemName = values[2].trim();
                String itemQuantityStr = values[8].replaceFirst("\\s+kg","");
                double itemQuantity = Double.parseDouble(itemQuantityStr)*1000;
                double itemPrice = Double.parseDouble(values[10])/1000;
                int itemTax = (int) (Double.parseDouble(values[9])*100);
                itemsList.add(new Item(itemName, itemQuantity, itemPrice, itemTax, null));
            }
        }
        return itemsList;
    }

    @Override
    public Integer sheetIndexIfNameFails() {
        return 4;
    }

    public int getOrderColumnIdx() {
        return 14;
    }
}
