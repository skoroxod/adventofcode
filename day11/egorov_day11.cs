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
        public static void Solve(string inp)
        {
            //Console.WriteLine(inp.Substring(10));
            var ds = inp.Split(',');
            var x = 0.0;
            var y = 0.0;
            var cos = Math.Cos(Math.PI / 6);
            var sin = Math.Sin(Math.PI / 6);
            var best = 0.0;
            foreach (var d in ds)
            {
                if (d == "n") y--;
                else if (d == "s") y++;
                else{
                    if (d.Contains("n"))
                        y -= sin;
                    if (d.Contains("s"))
                        y += sin;
                    if (d.Contains("w"))
                        x -= cos;
                    if (d.Contains("e"))
                        x += cos;
                }
                best = Math.Max(best, GetDist(x, y, cos, sin));

            }
            Console.WriteLine(best);
        }

        private static double GetDist(double x, double y, double cos, double sin)
        {
            if (x < 0) x = -x;
            if (y < 0) y = -y;

            var xSteps = x / cos;
            y = y - xSteps * sin;
            var res = xSteps + y;
            return res;
        }

        private static string input = @"";
        private static string Input => File.ReadAllText(TestContext.CurrentContext.TestDirectory + "\\input.txt");
        private static int[] InputInts => Input.ToInts();

        private static int[][] InputTable => Input.ToIntsTable();

        //static int[] InputLines => File.ReadAllLines(TestContext.CurrentContext.TestDirectory + "\\input.txt").Select(int.Parse).ToArray();
        private static string[] InputLines =>
            File.ReadAllLines(TestContext.CurrentContext.TestDirectory + "\\input.txt");

        public static int[] ToInts(this string text)
        {
            return text.Split(',').Select(int.Parse).ToArray();
        }

        public static int[][] ToIntsTable(this string text)
        {
            return text.ToLines().Select(line => line.Split().Select(int.Parse).ToArray()).ToArray();
        }

        public static string[] ToLines(this string text)
        {
            return text.Split(new[] { "\r", "\n" }, StringSplitOptions.RemoveEmptyEntries);
        }

        [Test]
        public static void RunSolve()
        {
            Solve("se,sw,se,sw,sw");
            Solve("ne,ne,ne");
            Solve("ne,ne,sw,sw");
            Solve("ne,ne,s,s");
            Solve("sw,sw,n,n");
            Solve("se,se,n,n");
            Solve(Input);
        }
    }
}