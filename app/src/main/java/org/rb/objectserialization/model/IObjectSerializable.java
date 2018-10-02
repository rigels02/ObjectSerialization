package org.rb.objectserialization.model;

import java.io.Serializable;

public interface IObjectSerializable  extends Serializable, Cloneable, Indexable{

    Object clone() throws CloneNotSupportedException;
}
