public class Town implements Comparable<Town>{
    
    /**
     * Town's name
     */
    private String name;
    
    /**
     * Constructor - Requires town's name.
     * @param name town's name
     */
    public Town(String name) {
        this.name = name;
    }
    
    /**
     * Returns the town's name
     * @return town's name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Copy constructor
     * @param templateTown an instance of Town
     */
    public Town(Town templateTown) {
        this(templateTown.name);
    }
    
    /**
     * Equals method for towns
     * @param object, object to be compared
     * @return true if the town names are equal, false if not
     */
    @Override
    public boolean equals(Object object) {
        Town town = (Town) object;
        return this.name.compareTo(town.name) == 0;
    }
     
    /**
     * Compare to method between towns
     * @param i another town to be compared
     * @return 0 if names are equal, a positive or negative number 
     * if the names are not equal
     */
    @Override
    public int compareTo(Town i) {
        return this.name.compareTo(i.name);
    }  
    
    /**
     * Hash code for town
     * @return the hash code for the name of the town
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }
    
    /**
     * String representation of town
     * @return the town name
     */
    @Override
    public String toString(){
        return name;
    }
}