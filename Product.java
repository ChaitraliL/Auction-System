package socketauction;

import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author admin
 */
public class Product implements Serializable{
    private int id;
    private String name;
    private String category;
    private float base_price;
    private float max_price;
    private String description;
    private int usrid;
    
    public Product(int id,String name,String category,float base_price,float max_price,String description,int usrid)
    {
        this.id=id;
        this.name=name;
        this.category=category;
        this.base_price=base_price;
        this.max_price=max_price;
        this.description=description;
        this.usrid=usrid;
    }
    
    public int getid()
    {
        return id;
    }
    
    public String getname()
    {
        return name;
    }
    
    public String getcategory()
    {
        return category;
    }
    
    public float getbase_price()
    {
        return base_price;
    }
    
    public float getmax_price()
    {
        return max_price;
    }
        
    public String getdescription()
    {
        return description;
    }
    
    public int getusrid()
    {
        return usrid;
    }
}
