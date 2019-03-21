package rpc.woo.framework.common;

import java.io.Serializable;
import java.util.Arrays;

public class ProtocolBean implements Serializable {
    private Object returnObject;
    private String interfaceRefName;
    private String methodName;
    private Object[] args;
    private String[] argRefNames;
    private String returnType;

    public Object getReturnObject() {
        return returnObject;
    }

    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    public String getInterfaceRefName() {
        return interfaceRefName;
    }

    public void setInterfaceRefName(String interfaceRefName) {
        this.interfaceRefName = interfaceRefName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String[] getArgRefNames() {
        return argRefNames;
    }

    public void setArgRefNames(String[] argRefNames) {
        this.argRefNames = argRefNames;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    @Override
    public String toString() {
        return "ProtocolBean{" +
                "returnObject=" + returnObject +
                ", interfaceRefName='" + interfaceRefName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", args=" + Arrays.toString(args) +
                ", argRefNames=" + Arrays.toString(argRefNames) +
                ", returnType='" + returnType + '\'' +
                '}';
    }
}
