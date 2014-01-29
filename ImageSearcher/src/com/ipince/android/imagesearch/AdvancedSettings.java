package com.ipince.android.imagesearch;

import java.io.Serializable;

import com.google.common.base.Strings;

public class AdvancedSettings implements Serializable {

    private static final long serialVersionUID = 4337223576478735722L;

    public enum Size {
        // TODO(ipince): horrible. These names should be localized.
        NONE("No Filter"),
        ICON("Icon"),
        SMALL("Small"),
        MEDIUM("Medium"),
        LARGE("Large"),
        XLARGE("X-Large"),
        XXLARGE("XX-Large"),
        HUGE("Huge");

        private String name;

        private Size(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public enum Color {
        NONE("No Filer"),
        BLACK("Black"),
        BLUE("Blue"),
        BROWN("Brown"),
        GRAY("Gray"),
        GREEN("Green"),
        ORANGE("Orange"),
        PINK("Pink"),
        PURPLE("Purple"),
        RED("Red"),
        TEAL("Teal"),
        WHITE("White"),
        YELLOW("Yellow");

        private String name;

        private Color(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public enum Type {
        NONE("No Filter"),
        FACE("Face"),
        PHOTO("Photo"),
        CLIPART("Clip-Art"),
        LINEART("Line Art");

        private String name;

        private Type(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
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
