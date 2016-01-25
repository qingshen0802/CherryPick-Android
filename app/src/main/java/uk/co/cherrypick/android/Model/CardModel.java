/**
 * AndTinder v0.1 for Android
 *
 * @Author: Enrique López Mañas <eenriquelopez@gmail.com>
 * http://www.lopez-manas.com
 * <p/>
 * TAndTinder is a native library for Android that provide a
 * Tinder card like effect. A card can be constructed using an
 * image and displayed with animation effects, dismiss-to-like
 * and dismiss-to-unlike, and use different sorting mechanisms.
 * <p/>
 * AndTinder is compatible with API Level 13 and upwards
 * @copyright: Enrique López Mañas
 * @license: Apache License 2.0
 */

package uk.co.cherrypick.android.Model;

import android.graphics.drawable.Drawable;

import java.net.URL;

import uk.co.cherrypick.android.Utiles.ApplicationConstants;
import uk.co.cherrypick.android.Utiles.ApplicationConstants.Network;

public class CardModel {

	private Integer deviceId;
    private int id;
	private String title;
	private String description;


	private Network network;
	private Double upfrontCost;
	private Double monthlyCost;
	private String contactLength;

	private Boolean network3g;
	private Boolean network4g;
	private String dataAllowance; // -1 = unlimited, 0 = none, greater than zero, Allowance in GB/Month
	private String smsAllowance;
	private String talkTimeAllowance;
    private URL basketUrl;



    //	private Drawable cardImageDrawable;
    private Drawable cardLikeImageDrawable;
    private Drawable cardDislikeImageDrawable;

    private OnCardDismissedListener mOnCardDismissedListener = null;

    private OnClickListener mOnClickListener = null;

    public interface OnCardDismissedListener {
        void onLike();

        void onDislike();

        void onMaybe();
    }

    public interface OnClickListener {
        void OnClickListener();
    }

    public CardModel(int id,Integer deviceId, String title, String description, Network network, Double upfrontCost, Double monthlyCost, String contactLength,
                     Boolean network3g, Boolean network4g, String dataAllowance, String smsAllowance, String talkTimeAllowance,
                     URL basketUrl) {
        this.id = id;
		this.deviceId = deviceId;
		this.title = title;
		this.description = description;
		this.network = network;
		this.upfrontCost = upfrontCost;
		this.monthlyCost = monthlyCost;
		this.contactLength = contactLength;
		this.network3g = network3g;
		this.network4g = network4g;
		this.dataAllowance = dataAllowance;
		this.smsAllowance = smsAllowance;
		this.talkTimeAllowance = talkTimeAllowance;
        this.basketUrl = basketUrl;
	}


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

    public ApplicationConstants.Network getNetwork() {
        return network;
    }

    public void setNetwork(ApplicationConstants.Network network) {
        this.network = network;
    }

    public Double getUpfrontCost() {
        return upfrontCost;
    }

    public void setUpfrontCost(Double upfrontCost) {
        this.upfrontCost = upfrontCost;
    }

    public Double getMonthlyCost() {
        return monthlyCost;
    }

    public void setMonthlyCost(Double monthlyCost) {
        this.monthlyCost = monthlyCost;
    }

	public String getContactLength() {
		return contactLength;
	}

	public void setContactLength(String contactLength) {
		this.contactLength = contactLength;
	}

    public Boolean getNetwork3g() {
        return network3g;
    }

    public void setNetwork3g(Boolean network3g) {
        this.network3g = network3g;
    }

    public Boolean getNetwork4g() {
        return network4g;
    }

    public void setNetwork4g(Boolean network4g) {
        this.network4g = network4g;
    }

    public String getDataAllowance() {
        return dataAllowance;
    }

    public void setDataAllowance(String dataAllowance) {
        this.dataAllowance = dataAllowance;
    }

    public String getSmsAllowance() {
        return smsAllowance;
    }

    public void setSmsAllowance(String smsAllowance) {
        this.smsAllowance = smsAllowance;
    }

    public String getTalkTimeAllowance() {
        return talkTimeAllowance;
    }


    public void setTalkTimeAllowance(String talkTimeAllowance) {
        this.talkTimeAllowance = talkTimeAllowance;
    }

    public URL getBasketUrl() {
        return basketUrl;
    }

    public void setBasketUrl(URL basketUrl) {
        this.basketUrl = basketUrl;
    }

    public String getDescription() {

		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


    public Drawable getCardLikeImageDrawable() {
        return cardLikeImageDrawable;
    }

    public void setCardLikeImageDrawable(Drawable cardLikeImageDrawable) {
        this.cardLikeImageDrawable = cardLikeImageDrawable;
    }

    public Drawable getCardDislikeImageDrawable() {
        return cardDislikeImageDrawable;
    }

    public void setCardDislikeImageDrawable(Drawable cardDislikeImageDrawable) {
        this.cardDislikeImageDrawable = cardDislikeImageDrawable;
    }

    public void setOnCardDismissedListener(OnCardDismissedListener listener) {
        this.mOnCardDismissedListener = listener;
    }

    public OnCardDismissedListener getOnCardDismissedListener() {
        return this.mOnCardDismissedListener;
    }

	public Integer getDeviceId() {
		return deviceId;
	}

	public void setDevice(Integer deviceId) {
		this.deviceId = deviceId;
	}



    public void setOnClickListener(OnClickListener listener) {
        this.mOnClickListener = listener;
    }

    public OnClickListener getOnClickListener() {
        return this.mOnClickListener;
    }
}
