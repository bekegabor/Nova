package com.gdf.diplomamunka.gaborbeke.nova.enums;

public enum Status {
        IN_PROGRESS("Folyamatban"){
                @Override
                public String getUserFriendlyStatus() {
                        return "Folyamatban";
                }
        },
        PENDING("Beérkezés alatt"){
                @Override
                public String getUserFriendlyStatus() {
                        return "Beérkezés alatt";
                }
        },
        CLOSED("Lezárt"){
                @Override
                public String getUserFriendlyStatus() {
                        return "Lezárt";
                }
        };

        private String statusName;

        Status(String statusName) {
                this.statusName = statusName;
        }

        public String getStatusName() {
                return statusName;
        }

        public abstract String getUserFriendlyStatus();
}
