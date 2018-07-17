/**
 *Administrator
 */
package com.ft.otp.manager.data.entity;

/**
 * 表信息说明
 *
 * @Date in Jan 7, 2013,3:00:23 PM
 * @version v1.0
 * @author BYL
 */
public class TableInfo {
    private String name = ""; // 表名
    private String autoIncrementedColumn = ""; // 自增长列
    String columns = ""; // 列集合 column1,column2,column3, 形式
    
    String sequence = "";
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the autoIncrementedColumn
     */
    public String getAutoIncrementedColumn() {
        return autoIncrementedColumn;
    }

    /**
     * @param autoIncrementedColumn the autoIncrementedColumn to set
     */
    public void setAutoIncrementedColumn(String autoIncrementedColumn) {
        this.autoIncrementedColumn = autoIncrementedColumn;
    }

	/**
	 * @return the columns
	 */
	public String getColumns() {
		return columns;
	}

	/**
	 * @param columns the columns to set
	 */
	public void setColumns(String columns) {
		this.columns = columns;
	}

	/**
	 * @return the sequence
	 */
	public String getSequence() {
		return sequence;
	}

	/**
	 * @param sequence the sequence to set
	 */
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

}
