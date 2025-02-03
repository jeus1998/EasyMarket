package easy.market.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class SecurityConst {
    public static final String ACCESS_TOKEN = "access";
    public static final String REFRESH_TOKEN = "refresh";
    public static final long ACCESS_TOKEN_EXPIRED_MS = 600000L;
    public static final long REFRESH_TOKEN_EXPIRED_MS = 86400000L;
    public static final int ACCESS_TOKEN_EXPIRED_S = 600;
    public static final int REFRESH_TOKEN_EXPIRED_S = 86400;
}
