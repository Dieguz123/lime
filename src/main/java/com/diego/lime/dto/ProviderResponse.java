package com.diego.lime.dto;

import com.diego.lime.model.Provider;
import jakarta.annotation.Nullable;

public record ProviderResponse(Long id, String name, @Nullable String logoPath) {

    public static ProviderResponse from(Provider provider) {
        return new ProviderResponse(provider.getId(), provider.getName(), provider.getLogoPath());
    }
}
