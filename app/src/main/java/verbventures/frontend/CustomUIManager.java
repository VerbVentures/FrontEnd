package verbventures.frontend;

import android.app.Fragment;
import android.os.Parcel;
import android.support.annotation.Nullable;

import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.ui.BaseUIManager;
import com.facebook.accountkit.ui.ButtonType;
import com.facebook.accountkit.ui.LoginFlowState;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.accountkit.ui.TextPosition;
/**
 * Created by Drew on 11/3/2017.
 */

public class CustomUIManager extends BaseUIManager {
    private final ButtonType confirmButton;
    private final ButtonType entryButton;
    private final TextPosition textPosition;

    public CustomUIManager(
            final ButtonType confirmButton,
        final ButtonType entryButton,
        final LoginType loginType,
        final TextPosition textPosition,
        final int themeResourceId) {
        super(-1);
        this.confirmButton = confirmButton;
        this.entryButton = entryButton;
        this.textPosition = textPosition;
    }

    private CustomUIManager(final Parcel source) {
        super(source);
        String s = source.readString();
        final ButtonType confirmButton = s == null ? null : ButtonType.valueOf(s);
        s = source.readString();
        final ButtonType entryButton = s == null ? null : ButtonType.valueOf(s);
        s = source.readString();
        final TextPosition textPosition = s == null ? null : TextPosition.valueOf(s);
        this.confirmButton = confirmButton;
        this.entryButton = entryButton;
        this.textPosition = textPosition;
    }

    @Override
    @Nullable
    public Fragment getHeaderFragment(final LoginFlowState state) {
        Fragment headerFragment;
        switch (state) {
            case PHONE_NUMBER_INPUT:
            case EMAIL_INPUT:
            case EMAIL_VERIFY:
            case SENDING_CODE:
            case SENT_CODE:
            case CODE_INPUT:
            case VERIFYING_CODE:
            case VERIFIED:
            case ACCOUNT_VERIFIED:
            case CONFIRM_ACCOUNT_VERIFIED:
            case CONFIRM_INSTANT_VERIFICATION_LOGIN:
                // insert appropriate customizations for headerFragment
            case ERROR:
                // handle appropriate error for headerFragment
            default:
                headerFragment = new Fragment();
        }

        return headerFragment;
    }

    public @Nullable ButtonType getButtonType(final LoginFlowState state) {
        switch (state) {
            case PHONE_NUMBER_INPUT:
                return entryButton;
            case EMAIL_INPUT:
                return entryButton;
            case CODE_INPUT:
                return confirmButton;
            default:
                return null;
        }
    }

    @Override
    public void onError(final AccountKitError error) {
        // handle error
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(confirmButton != null ? confirmButton.name() : null);
        dest.writeString(entryButton != null ? entryButton.name() : null);
        dest.writeString(textPosition != null ? textPosition.name() : null);
    }

    public static final Creator<CustomUIManager> CREATOR
            = new Creator<CustomUIManager>() {
        @Override
        public CustomUIManager createFromParcel(final Parcel source) {
            return new CustomUIManager(source);
        }

        @Override
        public CustomUIManager[] newArray(final int size) {
            return new CustomUIManager[size];
        }
    };
}
