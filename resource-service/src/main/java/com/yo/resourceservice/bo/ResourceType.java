package com.yo.resourceservice.bo;

public enum ResourceType {
    /**笔记类型的资源*/
    NOTE("1","NOTE"),
    /**文件类型的资源*/
    FILE("2","FILE"),
    /**收藏类型的资源*/
    FAVORITE("3","FAVORITE");

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String code;

    private String name;

    ResourceType(String code, String name){
        this.code = code;
        this.name = name;
    }

    public static boolean isContain(String name){
        for(ResourceType type : ResourceType.values()){
            if (type.getName().equals(String.valueOf(name))){
                return true;
            }
        }
        return false;
    }
}
