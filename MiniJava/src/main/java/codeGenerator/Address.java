package codeGenerator;

/**
 * Created by mohammad hosein on 6/28/2015.
 */

public class Address {
    public abstract class AddressType {
        abstract TypeAddress getAddressType();
    }

    public class Direct extends AddressType {
        @Override
        TypeAddress getAddressType() { return TypeAddress.Direct; }
    }

    public class Indirect extends AddressType {
        @Override
        TypeAddress getAddressType() { return TypeAddress.Indirect; }
    }

    public class Imidiate extends AddressType {
        @Override
        TypeAddress getAddressType() { return TypeAddress.Imidiate; }
    }

    public int num;
    public AddressType Type;
    public varType varType;

    public Address(int num, varType varType, TypeAddress Type) {
        this.num = num;
        setType(Type);
        this.varType = varType;
    }

    public Address(int num, varType varType) {
        this.num = num;
        setType(TypeAddress.Direct);
        this.varType = varType;
    }

    public String toString() {
        switch (getType()) {
            case Direct:
                return num + "";
            case Indirect:
                return "@" + num;
            case Imidiate:
                return "#" + num;
        }
        return num + "";
    }

    public TypeAddress getType() {
        return Type.getAddressType();
    }

    public void setType(TypeAddress typeAddress) {
        switch (typeAddress) {
            case Direct:
                Type = new Address.Direct();
                break;
            case Indirect:
                Type = new Address.Indirect();
                break;
            case Imidiate:
                Type = new Address.Imidiate();
                break;
            default:
                throw new IllegalArgumentException("address type non-existent");
        }
    }
}
