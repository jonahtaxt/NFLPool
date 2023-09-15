using ClosedXML.Excel;
using NFLPool.Interface;
using NFLPool.Model;

namespace NFLPool.Service
{
    public class ExcelReader : IExcelReader
    {
        private XLWorkbook? _wb;
        private IXLWorksheet? _worksheet;

        public PoolWeekScores? ReadFile(Stream xlsStream)
        {
            _wb = new XLWorkbook(xlsStream);
            _worksheet = _wb.Worksheets.FirstOrDefault(ws => ws.Name.Contains("CONCENTRADO JORNADA"));

            if (_worksheet is not null)
            {
                var participants = GetPoolParticipants();
                var poolTeams = GetPoolTeams(participants);
                GetMondayNightPoints(participants);
                return new PoolWeekScores(participants, poolTeams);
            }
            return null;
        }

        private static string GetExcelColumnName(int columnNumber)
        {
            string columnName = "";

            while (columnNumber > 0)
            {
                var modulo = (columnNumber - 1) % 26;
                columnName = Convert.ToChar('A' + modulo) + columnName;
                columnNumber = (columnNumber - modulo) / 26;
            }

            return columnName;
        }

        private void GetMondayNightPoints(List<Participant> participants)
        {
            for (var i = 1; i <= participants.Count; i++)
            {
                var pointsCell = _worksheet.Cell($"AI{i + 1}");
                if (pointsCell.Value.Type != XLDataType.Blank && pointsCell.Value.Type == XLDataType.Number)
                {
                    participants[i - 1].MondayNightPoints = Convert.ToInt32(pointsCell.Value.GetNumber());
                }
            }
        }

        private List<Participant> GetPoolParticipants()
        {
            var participants = new List<Participant>();

            var participantId = 1;
            var cell = _worksheet.Cell($"A{(participantId + 1)}");
            do
            {
                participants.Add(new Participant
                {
                    Id = participantId,
                    Name = cell.Value.GetText()
                });
                participantId++;
                cell = _worksheet.Cell($"A{participantId + 1}");
            } while (cell.Value.Type != XLDataType.Blank);

            return participants;
        }

        private List<PoolTeam> GetPoolTeams(List<Participant> participants)
        {
            var poolTeams = new List<PoolTeam>();

            for (var columnCounter = 2; columnCounter < 34; columnCounter++)
            {
                var column = GetExcelColumnName(columnCounter);
                var cell = _worksheet.Cell($"{column}1");
                if (cell.Value.Type != XLDataType.Blank && cell.Value.Type == XLDataType.Text)
                {
                    var poolTeam = new PoolTeam
                    {
                        TeamName = cell.Value.GetText()
                    };
                    for (var i = 1; i <= (participants.Count); i++)
                    {
                        cell = _worksheet.Cell($"{column}{i + 1}");
                        if (cell.Value.Type != XLDataType.Blank)
                        {
                            poolTeam.Participants.Add(i);
                        }
                    }
                    poolTeams.Add(poolTeam);
                }
            }

            return poolTeams;
        }
    }
}