package com.example.news.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CovidProperties {
    @SerializedName("定义")
    @Expose
    private String definition;

    @SerializedName("特征")
    @Expose
    private String characteristics;

    @SerializedName("包括")
    @Expose
    private String includes;

    @SerializedName("生存条件")
    @Expose
    private String livingConditions;

    @SerializedName("传播方式")
    @Expose
    private String infectionType;

    @SerializedName("应用")
    @Expose
    private String uses;

    public CovidProperties(String definition, String characteristics, String includes,
                           String livingConditions, String infectionType, String uses) {
        this.definition = definition;
        this.characteristics = characteristics;
        this.includes = includes;
        this.livingConditions = livingConditions;
        this.infectionType = infectionType;
        this.uses = uses;
    }

    public String getDefinition() {
        return definition;
    }

    public String getCharacteristics() {
        return characteristics;
    }

    public String getIncludes() {
        return includes;
    }

    public String getLivingConditions() {
        return livingConditions;
    }

    public String getInfectionType() {
        return infectionType;
    }

    public String getUses() {
        return uses;
    }
}
