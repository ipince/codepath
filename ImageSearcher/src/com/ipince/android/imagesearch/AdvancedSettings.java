package com.ipince.android.imagesearch;

import java.io.Serializable;

public class AdvancedSettings implements Serializable {

    public enum Size {
        NONE,
        SMALL;
    }

    private final Size size;
    private final String site;

    private AdvancedSettings(
            Size size,
            String site) {
        this.size = size;
        this.site = site;
    }

    public Size getSize() {
        return size;
    }

    private Builder newBuilder() {
        return new Builder();
    }

    public class Builder {

        private Size size;

        private Builder() {}

        public Builder setSize(Size size) {
            this.size = size;
            return this;
        }

        public AdvancedSettings build() {
            return new AdvancedSettings(
                    size == null ? Size.NONE : size,
                            site);
        }
    }
}
