﻿namespace NFLPool.Model;

public class Eagles : Team
{
    public Eagles()
    {
        PoolName = "PHILADELPHIA";
        Logo =
            "data:image/png;base64,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";
    }
}