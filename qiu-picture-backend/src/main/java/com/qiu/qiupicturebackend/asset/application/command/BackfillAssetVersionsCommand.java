package com.qiu.qiupicturebackend.asset.application.command;

import lombok.Data;

@Data
public class BackfillAssetVersionsCommand {

    private int batchSize = 500;
    private int maxBatches = 0;
    private boolean dryRun = false;
}
