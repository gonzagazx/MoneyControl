package com.example.MoneyControl.dto;

import com.example.MoneyControl.entity.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 100, message = "O nome pode ter no máximo 100 caracteres")
        String name,

        @NotNull(message = "O tipo é obrigatório")
        TransactionType type
) {
}