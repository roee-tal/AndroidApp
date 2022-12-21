package com.example.app;

import java.util.Comparator;


class Site
{
    private String id;
    private String name;
    private String image;
    private double rate;
    private String detail;
    private Location location;

    public Site(){};

    public Site(String id, String name, String image, double rate, String detail, Location location) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.detail = detail;
        this.location = location;
        this.rate = rate;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public static Comparator<Site> rateSort = new Comparator<Site>()
    {
        @Override
        public int compare(Site shape1, Site shape2)
        {
            double id1 = Double.valueOf(shape1.getRate());
            double id2 = Double.valueOf(shape2.getRate());

            return Double.compare(id1, id2);
        }
    };

    public static Comparator<Site> nameAscending = new Comparator<Site>()
    {
        @Override
        public int compare(Site shape1, Site shape2)
        {
            String name1 = shape1.getName();
            String name2 = shape2.getName();
            name1 = name1.toLowerCase();
            name2 = name2.toLowerCase();

            return name1.compareTo(name2);
        }
    };


}

//class sortType extends Site {
//    SortType type;
//    public sortType(SortType type) {
//        super();
//        this.type = type;
//    }
//}