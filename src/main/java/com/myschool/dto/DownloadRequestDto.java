package com.myschool.dto;

import lombok.Data;

/**
 * Created by medilox on 3/12/17.
 */
@Data
public class DownloadRequestDto {
    private String filename;
    private String content;
}
