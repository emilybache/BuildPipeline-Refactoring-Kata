using System;
using System.Collections.Generic;
using UntangledConditionals;

namespace BuildPipelineTests
{
    internal class CapturingLogger : ILogger
    {
        public List<string> Lines { get; } = new List<string>();

        public void Info(string message)
        {
            Lines.Add("INFO: " + message);
        }

        public void Error(string message)
        {
            Lines.Add("ERROR: " + message);
        }
    }
}