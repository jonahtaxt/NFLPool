﻿namespace NFLPool.Model;

public class Commanders : Team
{
    public Commanders()
    {
        PoolName = "WASHINGTON";
        Logo =
            "data:image/png;base64,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";
    }
}