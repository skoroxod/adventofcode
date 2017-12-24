using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
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

        public static void Solve(params string[] map)
        {
            var y = 0;
            var x = map[0].IndexOf("|");
            var dx = 0;
            var dy = 1;
            var res = new StringBuilder();
            var count = 0;
            while(count < 1000000)
            {
                x += dx;
                y += dy;
                var c = map[y][x];
                if (char.IsLetter(c))
                    res.Append(c.ToString());
                if (c == '+')
                {
                    var found = false;
                    var newdx = 0;
                    var newdy = 0;
                    
                    for (int nx = x-1; nx <=x+1; nx++)
                    for (int ny = y-1; ny <=y+1; ny++)
                    {
                        if (((nx == x) ^ (ny == y)) && (nx != x-dx || ny != y-dy) && map[ny][nx] != ' ')
                        {
                            if (newdx != 0 || newdy != 0)
                                throw new Exception();
                            newdx = nx - x;
                            newdy = ny - y;
                            //Console.Write(map[ny][nx]);
                            found = true;
                        }
                    }
                    if (!found) break;
                    dx = newdx;
                    dy = newdy;
                    //Console.WriteLine(x + " " + y + " " + dx + " " + dy);
                }
                if (c == ' ') break;
                //if (c != '-' && c != '|') Console.Write(c);
                count++;
            }
            Console.WriteLine(res);
            Console.WriteLine(count);
        }

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
    }
}