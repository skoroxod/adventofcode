using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.Remoting.Messaging;
using System.Threading;
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
        public class Program
        {
            private readonly string[] cmds;

            public Program(string[] cmds, int index)
            {
                this.cmds = cmds;
                regs['p'] = index;
            }


            public int SendCount = 0;
            int ip = 0;
            long[] regs = new long[128];
            long played = 0;
            long GetValue(string regOrValue)
            {
                if (int.TryParse(regOrValue, out var value)) return value;
                return regs[regOrValue[0]];
            }
            
            public bool ExecuteOne(Queue<long> input, Queue<long> output)
            {
                if (ip < 0 || ip >= cmds.Length) return false;
                var ps = cmds[ip].Split();
                var cmd = ps[0];
                var x = ps[1];
                if (cmd == "snd")
                {
                    output.Enqueue(GetValue(x));
                    SendCount++;
                }
                else if (cmd == "set") regs[x[0]] = GetValue(ps[2]);
                else if (cmd == "add") regs[x[0]] = regs[x[0]] + GetValue(ps[2]);
                else if (cmd == "mul") regs[x[0]] = regs[x[0]] * GetValue(ps[2]);
                else if (cmd == "mod") regs[x[0]] = regs[x[0]] % GetValue(ps[2]);
                else if (cmd == "rcv")
                {
                    if (!input.Any()) return false;
                    regs[x[0]] = input.Dequeue();
                }
                else if (cmd == "jgz" && GetValue(x)>0)
                {
                    ip = (int) (ip + GetValue(ps[2]) - 1);
                }
                ip++;
                //Console.WriteLine(regs.Skip('a').Take('z'-'a'+1).StrJoin(" "));
                return true;
            }
        }

        public static void Solve(params string[] cmds)
        {
            var q0 = new Queue<long>();
            var q1 = new Queue<long>();
            var p0 = new Program(cmds, 0);
            var p1 = new Program(cmds, 1);
            while(true)
            {
                var ok1 = p0.ExecuteOne(q0, q1);
                var ok2 = p1.ExecuteOne(q1, q0);
                if (!ok1 && !ok2) break;
            }
            Console.WriteLine(p1.SendCount);
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