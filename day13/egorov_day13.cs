using System;
using System.Collections.Generic;
using System.IO;
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
            Solve("0: 3", "1: 2", "4: 4", "6: 4");
            Solve(InputLines);
        }

        public static void Solve(params string[] input)
        {
            var layers = input.Select(line => line.Replace(": ", " ").Split().Select(int.Parse).ToArray())
                .Select(ps => (depth:ps[0], width:ps[1], period: ps[1]*2 - 2))
                .ToArray();
            var penalty = layers.Where(dw => dw.depth % dw.period == 0).Sum(dw => dw.depth * dw.width);
            Console.WriteLine("penalty " + penalty);
            
            var minDelay = Enumerable.Range(0, int.MaxValue)
                .First(delay => layers.All(dw => (dw.depth + delay) % dw.period != 0));
            Console.WriteLine("delay " + minDelay);
        }

        private static string[] InputLines =>
            File.ReadAllLines(TestContext.CurrentContext.TestDirectory + "\\input.txt");

    }
}