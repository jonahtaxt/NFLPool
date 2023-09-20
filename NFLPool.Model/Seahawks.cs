﻿namespace NFLPool.Model;

public class Seahawks : Team
{
    public Seahawks()
    {
        PoolName = "SEATTLE";
        Logo =
            "data:image/png;base64,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";
    }
}