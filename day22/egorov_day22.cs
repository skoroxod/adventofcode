using System;
using System.Collections.Generic;
using System.IO;
using System.IO.Compression;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Security.Cryptography.X509Certificates;
using System.Text;
using NUnit.Framework;

namespace AdventOfCode2017
{
    [TestFixture]
    public static class Problem
    {
        [Test]
        public static void RunSolve()
        {
            Solve(InputLines);
        }

        public static void Solve(params string[] lines)
        {
            var infected = new Dictionary<Vec, int>();
            for (int y = 0; y < lines.Length; y++)
            for (int x = 0; x < lines[0].Length; x++)
                if (lines[y][x] == '#') infected.Add(new Vec(x, y), 2);

            var pos = new Vec(lines.Length / 2, lines[0].Length / 2);
            var dir = 0;
            for (int i = 0; i < 10000000; i++)
                Simulate(infected, ref pos, ref dir);
            Console.WriteLine(infectedCount + " " + pos + " " + dir);
        }

        public static int infectedCount = 0;
        
        private static void Simulate(Dictionary<Vec, int> infected, ref Vec pos, ref int dir)
        {
            var dirDif = new[] { 3, 0, 1, 2 };
            var dirs = new[] { new Vec(0, -1), new Vec(1, 0), new Vec(0, 1), new Vec(-1, 0), };
            var state = infected.GetOrDefault(pos);
            dir = (dir + dirDif[state]) % 4;
            if (state == 1) infectedCount++;
            infected[pos] = (state + 1)%4;
            pos = pos + dirs[dir];
        }

        private static string[] InputLines =>
            File.ReadAllLines(TestContext.CurrentContext.TestDirectory + "\\input.txt");
    }
}