package com.ipince.android.imagesearch;

import java.io.Serializable;

import com.google.common.base.Strings;

public class AdvancedSettings implements Serializable {

    private static final long serialVersionUID = 4337223576478735722L;

    public enum Size {
        NONE,
        SMALL,
        MEDIUM,
        LARGE,
        XLARGE;
    }

    public enum Color {
        NONE,
        BLACK,
        BLUE,
        BROWN,
        GRAY,
        GREEN;
    }

    public enum Type {
        NONE,
        FACES,
        PHOTO,
        CLIPART,
        LINEART;
    }

    private Size size = Size.NONE;
    private Color color = Color.NONE;
    private Type type = Type.NONE;
    private String site = "";

    public AdvancedSettings() {}

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = Strings.nullToEmpty(site);
    }
}
