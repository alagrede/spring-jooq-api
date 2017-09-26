package com.example.demo.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SortField;
import org.jooq.TableField;
import org.jooq.UpdatableRecord;
import org.jooq.example.db.mysql.tables.records.EmployeeRecord;
import org.jooq.impl.TableImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.validation.FieldError;

import com.example.demo.service.exception.ValidationException;

public abstract class AbstractJooqService {

	@Autowired DSLContext dsl;

	@SuppressWarnings("unchecked")
	protected <T extends Record> T createRecord(Map<String, Object> properties, TableImpl<? extends Record> table) {
		  final Record inputRec = dsl.newRecord(table);
		  inputRec.fromMap(properties);
  		  return (T) dsl.insertInto(table).set(inputRec).returning().fetchOne();
	}

	@SuppressWarnings("unchecked")
	protected <T extends Record> T updateRecord(Map<String, Object> properties, TableImpl<? extends Record> table) {
		  final UpdatableRecord inputRec = (UpdatableRecord) dsl.fetchOne(table, ((TableField<Record, Long>)table.field("id")).eq(Long.parseLong(properties.get(table.field("id").getName()).toString())));
		  inputRec.fromMap(properties);
		  inputRec.store();
  		  return (T) inputRec;
	}

    protected Map<String, Object> convertToMap(Record record, TableField... fields) {
    	Map<String, Object> map = new HashMap<>();
    	for (TableField f : fields) {
    		map.put(f.getName(), record.getValue(f));
    	}
    	return map;
    }
	
    protected Collection<SortField<?>> getSortFields(Sort sortSpecification, TableImpl<? extends Record> table) {
        Collection<SortField<?>> querySortFields = new ArrayList<>();
 
        if (sortSpecification == null) {
            return querySortFields;
        }
 
        Iterator<Sort.Order> specifiedFields = sortSpecification.iterator();
 
        while (specifiedFields.hasNext()) {
            Sort.Order specifiedField = specifiedFields.next();
 
            String sortFieldName = specifiedField.getProperty();
            Sort.Direction sortDirection = specifiedField.getDirection();

            TableField tableField = (TableField) table.field(sortFieldName);
            SortField<?> querySortField = convertTableFieldToSortField(tableField, sortDirection);
            querySortFields.add(querySortField);
        }
 
        return querySortFields;
    }
 
    private SortField<?> convertTableFieldToSortField(TableField tableField, Sort.Direction sortDirection) {
        if (sortDirection == Sort.Direction.ASC) {
            return tableField.asc();
        }
        else {
            return tableField.desc();
        }
    }
    
	protected void validate(Map<String, Object> properties, String objectName, List<TableField> fields) throws ValidationException {
		List<FieldError> errors = new ArrayList<FieldError>();

		Map<String, Object> map = new HashMap<>();
    	for (TableField f : fields) {
    		// Make verification
    		if (properties.get(f.getName()) == null) {
    			FieldError e = new FieldError(objectName, f.getName(), "n'est pas présent");
    			errors.add(e);
    		} else {
    			if (f.getType().equals(Integer.class)) {
    				try {
    					Integer val = Integer.parseInt(String.valueOf(properties.get(f.getName())));			
    				} catch(NumberFormatException ex) {
    	    			FieldError e = new FieldError(objectName, f.getName(), "n'est pas une valeur correcte");
    	    			errors.add(e);    					
    				}
    			}
    		}
    		
    	}
    	if (errors.size() > 0) {
    		throw new ValidationException(errors);
    	}
	}

	
}
