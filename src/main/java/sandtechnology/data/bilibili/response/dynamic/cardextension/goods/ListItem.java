package sandtechnology.data.bilibili.response.dynamic.cardextension.goods;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListItem {

    @SerializedName("img")
    private String imgURL;

    @SerializedName("jumpLink")
    private String jumpLink;

    @SerializedName("type")
    private int type;

    @SerializedName("price")
    private int price;

    @SerializedName("itemsId")
    private int itemsId;

    @SerializedName("schemaPackageName")
    private String schemaPackageName;

    @SerializedName("wordJumpLinkDesc")
    private String wordJumpLinkDesc;

    @SerializedName("iconUrl")
    private String iconUrl;

    @SerializedName("id")
    private int id;

    @SerializedName("msource")
    private String msource;

    @SerializedName("brief")
    private String brief;

    @SerializedName("itemIdStr")
    private String itemIdStr;

    @SerializedName("iconName")
    private String iconName;

    @SerializedName("appName")
    private String appName;

    @SerializedName("descTags")
    private List<Object> descTags;

    @SerializedName("schemaUrl")
    private String schemaUrl;

    @SerializedName("oriPrice")
    private String oriPrice;

    @SerializedName("priceStr")
    private String priceStr;

    @SerializedName("jumpLinkDesc")
    private String jumpLinkDesc;

    @SerializedName("dynamicId")
    private long dynamicId;
    @SerializedName("shopGoodType")
    private int shopGoodType;

    @SerializedName("sourceType")
    private int sourceType;

    @SerializedName("name")
    private String name;

    @SerializedName("outerId")
    private int outerId;

    @SerializedName("adMark")
    private String adMark;

    @SerializedName("useAdWebV2")
    private boolean useAdWebV2;

    public String getImgURL() {
        return imgURL;
    }

    public String getJumpLink() {
        return jumpLink;
    }

    public int getType() {
        return type;
    }

    public int getPrice() {
        return price;
    }

    public int getItemsId() {
        return itemsId;
    }

    public String getSchemaPackageName() {
        return schemaPackageName;
    }

    public String getWordJumpLinkDesc() {
        return wordJumpLinkDesc;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public int getId() {
        return id;
    }

    public String getMsource() {
        return msource;
    }

    public String getBrief() {
        return brief;
    }

    public String getItemIdStr() {
        return itemIdStr;
    }

    public String getIconName() {
        return iconName;
    }

    public String getAppName() {
        return appName;
    }

    public List<Object> getDescTags() {
        return descTags;
    }

    public String getSchemaUrl() {
        return schemaUrl;
    }

    public String getOriPrice() {
        return oriPrice;
    }

    public String getPriceStr() {
        return priceStr;
    }

    public String getJumpLinkDesc() {
        return jumpLinkDesc;
    }

    public long getDynamicId() {
        return dynamicId;
    }

    public int getShopGoodType() {
        return shopGoodType;
    }

    public int getSourceType() {
        return sourceType;
    }

    public String getName() {
        return name;
    }

    public int getOuterId() {
        return outerId;
    }

    public String getAdMark() {
        return adMark;
    }

    public boolean isUseAdWebV2() {
        return useAdWebV2;
    }
}