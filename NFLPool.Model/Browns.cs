﻿namespace NFLPool.Model;

public class Browns : Team
{
    public Browns()
    {
        PoolName = "CLEVELAND";
        Logo =
            "data:image/png;base64,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";
    }
}