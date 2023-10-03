namespace NFLPool.Model;

public class PoolFile
{
    public string Name { get; set; } = string.Empty;

    public int Year()
    {
        return Convert.ToInt32(Name.Split('_')[1]); 
    } 
    public int Week()
    {
        return Convert.ToInt32(Name.Split('_')[2].Replace(".txt", string.Empty));
    }
}