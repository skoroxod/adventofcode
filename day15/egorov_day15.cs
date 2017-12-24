using System;
using System.Collections.Generic;
using System.Linq;
using NUnit.Framework;

namespace AdventOfCode2017
{
    [TestFixture]
    public static class Problem
    {
        [Test]
        public static void RunSolve()
        {
            Solve(65, 8921);
            Solve(722, 354);
        }

        public static void Solve(int aStart, int bStart)
        {
            var count = Gen(aStart, 16807, 4)
                .Zip(
                    Gen(bStart, 48271, 8), 
                    (a, b) => ((a^b) << 16) == 0)
                .Take(5000000)
                .Count(ok => ok);
            Console.WriteLine(count);
        }
        
        public static IEnumerable<int> Gen(int start, int mult, int check)
        {
            while(true)
            {
                start = (int)(((long)start * mult) % 2147483647);
                if (start % check == 0) yield return start;
            }
        }
    }
}