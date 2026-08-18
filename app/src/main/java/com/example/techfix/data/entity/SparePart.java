package com.example.techfix.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName="spare_parts", foreignKeys=@ForeignKey(entity=Branch.class,parentColumns="branchId",childColumns="branchId",onDelete=ForeignKey.CASCADE))
public class SparePart {
    @PrimaryKey(autoGenerate=true) private int partId; private int branchId; private String name; private int quantity;
    public SparePart(int branchId,String name,int quantity){this.branchId=branchId;this.name=name;this.quantity=quantity;}
    public int getPartId(){return partId;} public void setPartId(int v){partId=v;} public int getBranchId(){return branchId;} public String getName(){return name;} public int getQuantity(){return quantity;}
}